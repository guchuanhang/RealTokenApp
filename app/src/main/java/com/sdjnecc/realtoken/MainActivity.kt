package com.sdjnecc.realtoken

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.sdjnecc.realtoken.data.net.RetrofitHelper
import com.uber.autodispose.android.lifecycle.autoDispose
import com.zhpan.idea.ServerConfig
import com.zhpan.idea.net.common.ResponseObserver
import com.zhpan.idea.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        heatBeat()
    }


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
}
