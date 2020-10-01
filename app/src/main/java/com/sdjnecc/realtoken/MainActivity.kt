package com.sdjnecc.realtoken

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.sdjnecc.realtoken.data.net.RetrofitHelper
import com.uber.autodispose.android.lifecycle.autoDispose
import com.zhpan.idea.RealToken
import com.zhpan.idea.net.common.ResponseObserver
import com.zhpan.idea.utils.LogUtils
import com.zhpan.idea.utils.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

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
