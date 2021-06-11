package ru.alexanderkozlovskiy.test

import android.app.Application
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class app:Application() {
inner class ExceptionCatcher(var handler: Thread.UncaughtExceptionHandler):Thread.UncaughtExceptionHandler {

        override fun uncaughtException(p0: Thread, p1: Throwable) {
var info:String
try {
val p:PackageInfo = applicationContext!!.packageManager!!.getPackageInfo(
    applicationContext!!.packageName,
    0
)!!
    info ="version: ${p.versionName} version code: ${if(Build.VERSION.SDK_INT<Build.VERSION_CODES.P)p.versionCode else p.longVersionCode}\n"
}
catch (e: Exception) {
    info ="version: 1.2 version code: 2\n"
}
            info+="version of android ${Build.VERSION.SDK_INT}\n"
val outputStream:ByteArrayOutputStream = ByteArrayOutputStream()
p1.printStackTrace(PrintStream(outputStream))
            info+="thread ${p0.toString()} and problems are:\n${outputStream.toString()}"
val i:Intent=Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("k.sasha1994@yandex.ru"))
i.putExtra(Intent.EXTRA_SUBJECT, "crash of test app")
            i.putExtra(Intent.EXTRA_TEXT, info)
i.flags=Intent.FLAG_ACTIVITY_NEW_TASK
try {
startActivity(i)
    //handler.uncaughtException(p0,p1)
    System.exit(0)
}
catch (e: Exception) {
//Toast.makeText(applicationContext,"Не удалось отправить лог: Ошибка ${e.message}",1).show()
    handler.uncaughtException(p0, e)
}
            }
}
    override fun onCreate() {
        super.onCreate()
if(Thread.getDefaultUncaughtExceptionHandler() !is ExceptionCatcher) Thread.setDefaultUncaughtExceptionHandler(ExceptionCatcher(Thread.getDefaultUncaughtExceptionHandler()))
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
}


    override fun onTerminate() {
        super.onTerminate()

    }
}
