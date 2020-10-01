package com.sdjnecc.realtoken

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhpan.idea.RealToken
import com.zhpan.idea.ServerConfig
import com.zhpan.idea.TokenBean
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @ClassName LoginActivity
 * @Description 登录页面
 * @Author guchu
 * @Date 2020/10/1 16:27
 * @Version 1.0
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            //登录成功，获取到 用于校验的信息后，更新realToken
            var tokenBean = TokenBean()
            tokenBean.deviceName = "device_0001"
            tokenBean.secretKey = "eiowoidkuuelwlwlwl"
            RealToken.updateToken(tokenBean)
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

    }

}