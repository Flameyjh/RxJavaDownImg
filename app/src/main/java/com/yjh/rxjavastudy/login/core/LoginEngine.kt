package com.yjh.rxjavastudy.login.core

import com.yjh.rxjavastudy.login.bean.ResponseResult
import com.yjh.rxjavastudy.login.bean.SuccessBean
import io.reactivex.rxjava3.core.Observable

class LoginEngine {


    companion object{

        //返回 起点
        fun login(name: String, pwd: String): Observable<ResponseResult>{

            //最终返回 总Bean
            val responseResult = ResponseResult()

            if ("yjh".equals(name) && "123456".equals(pwd)){ //登陆成功
                responseResult.data = SuccessBean(98765, "yjh登陆成功")
                responseResult.code = 200
                responseResult.message = "登陆成功"

            }else{ //登陆失败
                responseResult.data = null
                responseResult.code = 404
                responseResult.message = "登陆失败"
            }

            //起点
            return Observable.just(responseResult)
        }
    }
}