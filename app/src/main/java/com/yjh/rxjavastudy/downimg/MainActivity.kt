package com.yjh.rxjavastudy.downimg

import android.app.ProgressDialog
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL

//如果采用传统方式完成图片加载，每位开发者的思想都不一样。
//比如：A同学采用线程池，B同学采用new Thread + Handler，C同学采用古老的方式。这样后面开发者接收就很痛苦。
//Rx思维：起点——需求1——需求2——终点

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val PATH = "https://www.baidu.com/img/bdlogo.png"

    //弹出加载框（正在加载中）
    lateinit var progressDialog: ProgressDialog
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.image)
    }

    /*
    * 显示图片
    * */
    fun showImageAction(view: View) {
        //TODO 第二步 分发 PATH
        //起点
        Observable.just(PATH)

            //TODO 第三步
            //需求：001 图片下载需求 PATH -> Bitmap
            .map(object : Function<String, Bitmap> {
                override fun apply(t: String?): Bitmap? { //这里的t就是传入的PATH
                    try {
                        Thread.sleep(2000) //睡眠2秒以便观察效果

                        val url = URL(t)
                        val httpURLConnection = url.openConnection() as HttpURLConnection
                        httpURLConnection.connectTimeout = 5000 //设置请求连接时长为5秒
                        val responseCode = httpURLConnection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val inputStream = httpURLConnection.inputStream
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            return bitmap
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return null
                }

            })

            //需求：002 加水印
            .map(object : Function<Bitmap, Bitmap> {
                override fun apply(t: Bitmap): Bitmap? {
                    val paint = Paint()
                    paint.setColor(Color.RED)
                    paint.textSize = 24F
                    return drawTextToBitmap(t, "yjhyjh", paint, 88F, 88F)
                }
            })

            //需求：003 日志记录
            .map(object : Function<Bitmap, Bitmap> {
                override fun apply(t: Bitmap?): Bitmap? {
                    Log.e(TAG, "什么时候下载了图片 aplay: " + System.currentTimeMillis())
                    return t
                }

            })

            //给上面分配异步线程（图片下载操作）
            .subscribeOn(Schedulers.io())

            //给终点分配 Android 主线程
            .observeOn(AndroidSchedulers.mainThread())

            //关联：观察者设计模式   关联 起点 和 终点 == 订阅
            .subscribe(

                //终点
                object : Observer<Bitmap> {
                    override fun onSubscribe(d: Disposable?) { //订阅成功

                        //TODO 第一步
                        //加载框
                        progressDialog = ProgressDialog(this@MainActivity)
                        progressDialog.setTitle("RXJava run 正在加载中..")
                        progressDialog.show()
                    }

                    //TODO 第四步 显示图片
                    override fun onNext(t: Bitmap?) { //上一层给我的响应
                        image.setImageBitmap(t)
                    }

                    override fun onError(e: Throwable?) { //链条发生异常
                        System.out.println("error")
                    }

                    //TODO 第五步
                    override fun onComplete() { //链条全部结束
                        if (progressDialog != null)
                            progressDialog.dismiss()
                    }

                })
    }

    //加水印，图片上绘制文字
    private fun drawTextToBitmap(
        bitmap: Bitmap, text: String, paint: Paint, paddingLeft: Float, paddingTop: Float
    ): Bitmap? {
        var bitmap = bitmap
        var bitmapConfig: Bitmap.Config? = bitmap.config
        paint.setDither(true) // 获取跟清晰的图像采样
        paint.setFilterBitmap(true) // 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, paddingLeft, paddingTop, paint)
        return bitmap
    }


    /*
    * 常用操作符
    * 这里本来是想利用RxJava顺序输出"AAA", "BBB", "CCC"
    * 结果发现kotlin的字符数组和Java不一样，遂失败
    * */
    fun action(view: View) {
        val strings: Array<String> = arrayOf("AAA", "BBB", "CCC")


        //起点
        Observable.fromArray(strings)!!

            //订阅：起点 和 终点
            .subscribe(object : Consumer<Array<String>>{
                override fun accept(t: Array<String>?) {
                   Log.d(TAG, "accept: " + t.toString())
                }

            })
    }
}
