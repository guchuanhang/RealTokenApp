package com.sdjnecc.realtoken

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhpan.idea.RealToken
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
            //登录成功后，返回token信息
            var tokenBean = TokenBean()
            tokenBean.tokenStr = "ddss1224w3w"
            tokenBean.refreshTokenStr = "eiowoidkuuelwlwlwl"
            RealToken.updateToken(tokenBean)
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

    }

}