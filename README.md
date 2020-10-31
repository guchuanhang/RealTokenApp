# RealToken  Oauth2.0版本
    登录完成后，给客户端返回token、refreshToken.
    每一次网络请求，都需要在http header中带上token
    token过期后，需要使用refreshToken进行请求，获取新的token、refreshToken
    refreshToken过期后，重新登录

# 配置方法

## app 中引用 realtoken

1. 将realtoken复制到app同级目录

    1.1  修改settings.gradle

    添加以下内容

        include ':realtoken'



2. 修改settings.gradle， 添加

    ```
    include ':realtoken'
    ```
3. 在app build.gradle dependencies中添加
    ```
    implementation project(path: ':realtoken')
    ```

## app 中创建必要的文件

1. 创建IdeaApiService.java 并放入需要请求的网络接口（Retrofit2.0格式）

   eg.
    ```
    public interface IdeaApiService {

        @FormUrlEncoded
        @POST("device/ping")
        Observable<List<Void>> heartBeat(@Field("device_name") String deviceName);
    ...
    }

    ```

2. 创建 RetrofitHelper.java 并配置所有请求的公共url路径

    SplashActivity 为 APP启动页面， 当refreshToken过期后，跳转到首页

    其内容如下：

    ```

    public class RetrofitHelper {

        private static IdeaApiService mIdeaApiService;

        public static IdeaApiService getApiService() {
            if (mIdeaApiService == null)

                mIdeaApiService = new IdeaApiProxy().getApiService(IdeaApiService.class,
                        ServerConfig.BASE_URL, new IGlobalManager() {
                            @Override
                            public void logout() {
                                RealToken.clearToken();
                                Intent intent = new Intent(
                                        Utils.getContext(),
                                        SplashActivity.class
                                );
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Utils.getContext().startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                LogUtils.d("logout");
                            }

                            @Override
                            public void tokenRefresh(TokenBean response) {
                                LogUtils.d("tokenRefresh");
                                RealToken.updateToken(response);
                            }
                        });
            return mIdeaApiService;
        }
    }

    ```

3. realtoken 配置网络请求地址

        public class ServerConfig {
            public static String BASE_URL = "<网络请求的前缀>";

4. realtoken 在CommonService 定义刷新token的接口

        public interface CommonService {
            @FormUrlEncoded
            @POST("refreshToken")
            Observable<TokenBean> refreshToken(@Field("refresh_token") String refreshToken);
        }


## App.kt 初始化realtoken

1. 如下所示


    ```
    class App : Application() {
		override fun onCreate() {
		    super.onCreate()
            ...
            RealToken.init(this)
         }
    }
    ```

## 在http 拦截器中，定义客户端和服务器端认证需要的token

1. 在HttpHeaderInterceptor.intercept()传递服务器端验证客户端需要使用的参数

    eg.

       public class HttpHeaderInterceptor implements Interceptor {

            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = "";
                if (null != ServerConfig.instance.tokenBean) {
                    token = ServerConfig.instance.tokenBean.tokenStr;
                    if (TextUtils.isEmpty(token)) {
                        token = "";
                    }
                }
                Request request = chain.request().newBuilder()
                        .header("token", token)
                        .header("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        }


## http 请求方法


    private fun heatBeat() {
        RetrofitHelper.getApiService()
            .heartBeat(
                "device_0001"
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this as LifecycleOwner, Lifecycle.Event.ON_DESTROY)
            .subscribe(object : ResponseObserver<List<Void>>() {
                override fun onSuccess(response: List<Void>) {
                    LogUtils.d("TAG", "heatbeat")
                }

                override fun onFail(message: String?) {
                    super.onFail(message)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }
            })
    }
# 高级设置

## 服务器端响应

### 服务器响应格式
这里默认的http响应格式为：

    {
     "status": 100, //状态码
     "msg": "",    //如果请求出错，会有信息
     "data": {}    //请求成功，返回值
    }


如果服务器端和框架当前字段不一致，可以修改 BasicResponse.java

###  服务器响应status表示不同（必看）

可以修改ErrorCode.java 中定义的数字

    public class ErrorCode {
        /**
        * request success
        */
        public static final int SUCCESS = 100;   //请求成功

        public static final int TOKEN_EXPIRE = 1010; //token 过期
        public static final int REFRESH_TOKEN_EXPIRE = 1011; //refreshToken 过期
## 请求失败的处理

### 获取失败原因
请求失败，可以通过 ResponseObserver.onFail 直接拿到失败原因

    ResponseObserver<List<Void>>() {
                    override fun onSuccess(response: List<Void>) {
                        LogUtils.d("TAG", "heatbeat")
                        ToastUtils.show("调用成功")
                    }

                    override fun onFail(message: String?) {
                        //调用失败原因
                        super.onFail(message)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                    }
                })


### 屏蔽Toast失败提示

默认会进行toast提示msg信息。如果要取消该功能，可以屏蔽掉ResponseObserver.onFail中对 super.onFail()的调用

需要精细化的处理，可以参考ResponseObserver.onError() 并 override onError进行处理

# Token方式的一般过程

## LoginActivity.kt

  获取到用于校验的信息后，更新realToken(一般是登录成功后，返回结果)

    class LoginActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
                  btnLogin.setOnClickListener {
            //获取到 用于校验的信息后，更新realToken
            var tokenBean = TokenBean()
            //TODO  这里应该是登录后，返回的token、refreshToken.
            tokenBean.tokenStr = "ssssyyyyybbbtttt"
            tokenBean.refreshTokenStr = "eiowoidkuuelwlwlwl"
            RealToken.updateToken(tokenBean)
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

        }

    }

## MainActivity.kt

在用户logout的时候，要清除本地保存的tokenBean信息

    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            btnTest.setOnClickListener {
                testApi()
            }
            btnLogout.setOnClickListener {
                logout()
            }
        }
        private fun logout() {
            //清空保存在本地的登录信息
            RealToken.clearToken()
            this.finish()// 退出APP（activity stack 中只有这个一个页面）
        }
        private fun testApi() {
            RetrofitHelper.getApiService()
                .heartBeat(
                    "testDeviceName"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this as LifecycleOwner, Lifecycle.Event.ON_DESTROY)
                .subscribe(object : ResponseObserver<List<Void>>() {
                    override fun onSuccess(response: List<Void>) {
                        LogUtils.d("TAG", "heatbeat")
                        ToastUtils.show("调用成功")
                    }

                    override fun onFail(message: String?) {
                        super.onFail(message)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                    }
                })
        }
    }

# 其他

## autoDispose报错

    Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target ...

解决办法

在app  build.gradle android标签下添加

        compileOptions {
            sourceCompatibility = 1.8
            targetCompatibility = 1.8
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }




该应用fork于[zhpanvip/Retrofit2](https://github.com/zhpanvip/Retrofit2) ,特别标注。


## License
```
 Copyright 2020, guchuanhang

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```



