package ru.alexanderkozlovskiy.test
        import android.Manifest
        import android.app.DownloadManager
        import android.content.BroadcastReceiver
        import android.content.Context
        import android.content.Intent
        import android.content.IntentFilter
        import android.content.pm.PackageManager
        import android.net.Uri
        import android.os.Bundle
        import android.os.Environment
          import androidx.appcompat.app.AppCompatActivity

class ActivityMain : AppCompatActivity() {
            private var loadId:Long=-1
            private lateinit var dM:DownloadManager
            private val loadReciever:
                    BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                        if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                        val i:Intent=Intent(Intent.ACTION_SEND)
                        i.putExtra(Intent.EXTRA_STREAM,dM.getUriForDownloadedFile(loadId))
                        i.type="image/jpg"
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        i.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                        startActivity(i)
                    }
                }
            }
            val linkUri:Uri=Uri.parse("http://cron.me.fo/cron2/tmpimg_new/news_pic_093aa62ed3b0967f59c7464fec5ec9ec-re.jpg")
            fun loadUrl(uri: Uri) {
                val request: DownloadManager.Request = DownloadManager.Request(uri)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,uri.lastPathSegment)
                loadId = dM.enqueue(request)
            }
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                //val a:Int=1/0
                dM=applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                registerReceiver(loadReciever, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                else loadUrl(linkUri)
            }

            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
            ) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED) loadUrl(linkUri)
            }

            override fun onDestroy() {
                unregisterReceiver(loadReciever)
                super.onDestroy()
            }
        }
