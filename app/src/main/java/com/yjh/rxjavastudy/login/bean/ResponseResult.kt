package com.yjh.rxjavastudy.login.bean

data class ResponseResult constructor(var data: SuccessBean? = null, var code: Int = 0, var message: String = "")