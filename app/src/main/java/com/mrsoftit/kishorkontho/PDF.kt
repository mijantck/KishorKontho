package com.mrsoftit.kishorkontho

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_p_d_f.*
import java.io.File


class PDF : AppCompatActivity() {

    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    companion object {
        private const val PDF_SELECTION_CODE = 99
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f)


        var bundle :Bundle ?=intent.extras
        var pdfurl = bundle!!.getString("bookPDF")

        PRDownloader.initialize(applicationContext)




    /*   progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        pdfurl?.let {
            downloadPdfFromInternet(it, getRootDirPath(this), fileName)
        }*/


        progressBar.visibility = View.VISIBLE

      pdfurl?.let {
            storage.getReferenceFromUrl(it)
                .getBytes(900000000)
                .addOnSuccessListener { bytes ->
                    pdfView.fromBytes(bytes)
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load()

                    progressBar.visibility = View.GONE


                }
                .addOnFailureListener { e -> Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show() }
        }
    }


    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
                url,
                dirPath,
                fileName
        ).build()
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Toast.makeText(this@PDF, "downloadComplete", Toast.LENGTH_LONG)
                                .show()
                        val downloadedFile = File(dirPath, fileName)
                        progressBar.visibility = View.GONE
                        showPdfFromFile(downloadedFile)
                    }

                    override fun onError(error: Error?) {
                        Toast.makeText(
                                this@PDF,
                                "Error in downloading file : $error",
                                Toast.LENGTH_LONG
                        )
                                .show()
                    }
                })
    }

    private fun showPdfFromFile(file: File) {
        pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageError { page, _ ->
                    Toast.makeText(
                            this@PDF,
                            "Error at page: $page", Toast.LENGTH_LONG
                    ).show()
                }
                .load()
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                    context.applicationContext,
                    null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    private fun showPdfFromUri(uri: Uri?) {
        pdfView.fromUri(uri)
                .defaultPage(0)
                .spacing(10)
                .load()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfFromStorage = data.data
            showPdfFromUri(selectedPdfFromStorage)
        }
    }

}