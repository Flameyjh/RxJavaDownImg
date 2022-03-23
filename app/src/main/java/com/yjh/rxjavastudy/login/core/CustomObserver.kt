package com.yjh.rxjavastudy.login.core

import com.yjh.rxjavastudy.login.bean.ResponseResult
import com.yjh.rxjavastudy.login.bean.SuccessBean
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

abstract class CustomObserver: Observer<ResponseResult> {

    abstract fun success(successBean: SuccessBean)
    abstract fun error(message: String)

    override fun onSubscribe(d: Disposable?) {

    }

    override fun onNext(t: ResponseResult) {
        if (t.data == null){
            error(t.message + "请求失败，请检查日志..")
        }else{
            success(t.data!!)
        }
    }

    override fun onError(e: Throwable?) {
        error(e?.message + "请检查日志，链条错误详情")
    }

    override fun onComplete() {

    }

}