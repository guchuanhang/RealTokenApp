# RealToken
使用Retrofit2框架进行网络请求时，客户端实时生成token。

token等服务器校验的参数都在Http请求的Header中。

## 服务器响应

服务器响应json数据格式如下

    ```
    {
     "status": 100,
        "msg": "",
     "data": {}
    }
    ```
如果服务器端和框架当前字段不一致，可以修改 BasicResponse.java。

这里data可以{}、也可以是[ ]

### 请求结果处理

1. 如果status == 100 直接拿到data、如果失败可以通过onFail获取到msg字段的数据

2. 失败后，默认会进行toast提示msg信息。如果要屏蔽onFail中msg提示，屏蔽掉super.onFail()的调用即可，如果要针对异常进行更精细化的处理，可以参考ResponseObserver.onError()  override onError。

## 使用方法：

### 在项目中引入realtoken library

1. 将realtoken复制到app同级目录
2. 复制project build.gradle中的类库版本信息
    ```
    ext {
        // Sdk and tools
        compileSdkVersion = 28
        minSdkVersion = 21
        targetSdkVersion = 28

        buildToolsVersion = '29.0.2'
        releaseVersionCode = 1
        releaseVersionName = '1.0.0'

        rxjava2Version = '2.0.8'
        retrofit2Version = '2.2.0'
        rxlifecycle = '2.2.1'
        gsonVersion = '2.8.0'

        rxjava2adapter = '1.0.0'
        okhttp3interceptor = '3.10.0'
        autodispose = '1.4.0'

    }
    ```
3. 修改settings.gradle， 添加

    ```
    include ':realtoken'
    ```
4. 在app build.gradle dependencies中追加
    ```
    api project(path: ':realtoken')
    ```


### 创建两个必须要使用的文件

1. IdeaApiService

    这里面放入封装的retrofit2接口 eg.
    ```
    public interface IdeaApiService {

    @FormUrlEncoded
    @POST("device/ping")
    Observable<List<Void>> heartBeat(@Field("device_name") String deviceName);
    ...
    }

    ```

2. RetrofitHelper

    修改BASE_URL为自己在项目中使用到的请求地址
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
    ```

### 初始化realtoken library



        class App : Application() {
            override fun onCreate() {
            super.onCreate()
            RealToken.init(this,mapStr)
      }
    }


如果稍后设置参数，这里可以调用RealToken.init(this).获取参数后，调用
RealToken.setMsg()进行设置


### 定义token生成规则

1. 在ServerConfig中添加需要保存的变量


        public class ServerConfig {
        public static ServerConfig instance = new ServerConfig();
        public String DEVICE_NAME;
        public String SECRET_KEY;

        private ServerConfig() {
            DEVICE_NAME = (String) PreferencesUtil.get(RealToken.getContext(), "device_name", "");
          SECRET_KEY = (String) PreferencesUtil.get(RealToken.getContext(), "secret_key", "");
         }


2.在HttpHeaderInterceptor.intercept(),生成和传递服务器端验证客户端需要使用的参数


    public class HttpHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Long timestamp = System.currentTimeMillis() / 1000;
        String token = KeyTools.getMD5(String.format("%d&%s", timestamp, ServerConfig.instance.SECRET_KEY)).toLowerCase();

        Request request = chain.request().newBuilder()
                .header("devicename", ServerConfig.instance.DEVICE_NAME)
                .header("token", token)
                .addHeader("timestamp", "" + timestamp)
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
    }


### http 请求

    private fun heatBeat() {
        RetrofitHelper.getApiService()
            .heartBeat(
                ServerConfig.instance.DEVICE_NAME
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


这里如果autoDispose报错

    Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target ...

在app  build.gradle android标签下添加

    compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }



该应用fork于[zhpanvip/Retrofit2](https://github.com/zhpanvip/Retrofit2/edit/master/README.md) ,特别标注。


## License
```
 Copyright 2018, guchuanhang

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



