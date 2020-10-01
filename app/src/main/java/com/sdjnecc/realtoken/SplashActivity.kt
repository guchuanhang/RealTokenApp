package com.sdjnecc.realtoken

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhpan.idea.ServerConfig

/**
 * @ClassName SplashActivity
 * @Description 启动页面
 * @Author guchu
 * @Date 2020/10/1 16:27
 * @Version 1.0
 */
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