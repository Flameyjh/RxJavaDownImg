package com.yjh.rxjavastudy.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yjh.rxjavastudy.downimg.R
import com.yjh.rxjavastudy.login.bean.SuccessBean
import com.yjh.rxjavastudy.login.core.CustomObserver
import com.yjh.rxjavastudy.login.core.LoginEngine

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        * TODO 需求：如果登陆成功，只想拿到成功bean；如果登陆失败，只想拿到message
        *  */
        LoginEngine.login("yjh", "123456")
            .subscribe(object : CustomObserver(){
                override fun success(successBean: SuccessBean) {
                    Log.d("LoginActivity", "成功的Bean详情：SuccessBean: " + successBean.toString())
                }

                override fun error(message: String) {
                    Log.d("LoginActivity", "失败的message详情: error: " + message)
                }

            })
    }
}