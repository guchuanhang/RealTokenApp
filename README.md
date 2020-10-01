# RealToken
    使用Retrofit2框架进行网络请求时，基于密码、时间等信息，客户端实时生成token。服务器端基于token，完成对客户端访问权限的认证。

    服务器校验的token等参数都在Http请求的Header中。
# 配置方法

## app 中引用 realtoken 

1. 将realtoken复制到app同级目录
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

    其内容如下：

    ```
    public class RetrofitHelper {
        public static String BASE_URL = "<目标服务器地址>";

        private static IdeaApiService mIdeaApiService;

        public static IdeaApiService getApiService() {
            if (mIdeaApiService == null) {
                 mIdeaApiService = RetrofitService.getRetrofitBuilder(BASE_URL)
                    .build().create(IdeaApiService.class);
         }

            return mIdeaApiService;
         }
    }
    ```

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

## 定义客户端与服务器进行token校验时需要提前知道的信息(eg.密码)

1. 修改TokenBean.java 字段信息


        public class TokenBean implements Serializable {
            public String deviceName;
            public String secretKey;

        }


## 在http 拦截器中，定义客户端和服务器端认证需要的内容

1. 在HttpHeaderInterceptor.intercept(),生成和传递服务器端验证客户端需要使用的参数

    eg.

        public class HttpHeaderInterceptor implements Interceptor {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Long timestamp = System.currentTimeMillis() / 1000;
                String token = "";
                if (null != ServerConfig.instance.tokenBean) {
                    token = KeyTools.getMD5(String.format("%d&%s", timestamp, ServerConfig.instance.tokenBean.secretKey)).toLowerCase();
                }

                String deviceName = "";
                if (null != ServerConfig.instance.tokenBean) {
                    deviceName = ServerConfig.instance.tokenBean.deviceName;
                }
                Request request = chain.request().newBuilder()
                        .header("devicename", deviceName)
                        .header("token", token)
                        .addHeader("timestamp", "" + timestamp)
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

###  服务器响应status表示不同

可以修改ErrorCode.java 中定义的数字

    public class ErrorCode {
        /**
        * request success
        */
        public static final int SUCCESS = 100;


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

 ## SplashActivitykt
 判断tokenBean是否为null，如果是跳转到登录页面，否则跳转到主页面

    class SplashActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash)
            if (isLogon()) {
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }
        //判断是否登录
        private fun isLogon(): Boolean {
            ServerConfig.instance.tokenBean ?: return false
            return true
        }
    }

## LoginActivity.kt
  
  获取到用于校验的信息后，更新realToken(一般是提取的输入框内容)

    class LoginActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            btnLogin.setOnClickListener {
                //获取到 用于校验的信息后，更新realToken
                var tokenBean = TokenBean()
                tokenBean.deviceName = "device_0001"
                tokenBean.secretKey = "eiowoidkuuelwlwlwl"
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
            RealToken.clearMsg()
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



