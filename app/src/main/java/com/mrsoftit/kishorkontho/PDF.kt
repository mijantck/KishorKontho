package com.mrsoftit.kishorkontho

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.facebook.ads.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_p_d_f.*
import java.io.File


class PDF : AppCompatActivity() {

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var doubleBackToExitPressedOnce = false

    companion object {
        private const val PDF_SELECTION_CODE = 99
    }

    private val TAG: String = PDF::class.java.getSimpleName()

    var urlup = String()
    private var adView: AdView? = null
    private var interstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage(" please wait...")
        progressDialog.show()


        var bundle :Bundle ?=intent.extras
        var pdfurl = bundle!!.getString("bookPDF")

        interstitialAd = InterstitialAd(this, "IMG_16_9_APP_INSTALL#2454246528166965_2454247438166874")

        if (pdfurl != null) {
            urlup = pdfurl
        }


        PRDownloader.initialize(applicationContext)




    /*   progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        pdfurl?.let {
            downloadPdfFromInternet(it, getRootDirPath(this), fileName)
        }*/

        storage.getReferenceFromUrl(urlup)
            .getBytes(900000000)
            .addOnSuccessListener { bytes ->
                pdfView.fromBytes(bytes)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(false)
                    .load()

              //  progressBar.visibility = View.GONE
                progressDialog.dismiss()


            }
            .addOnFailureListener { e -> Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }


        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad?) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad?, adError: AdError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage())
            }

            override fun onAdLoaded(ad: Ad?) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                interstitialAd!!.show()
            }

            override fun onAdClicked(ad: Ad?) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad?) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }


        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build());

        showBanner()



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
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit this page ?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { arg0, arg1 ->
                    setResult(Activity.RESULT_OK, Intent().putExtra("EXIT", true))
                    finish()
                }).create().show()
    }


    private fun showBanner() {

        val adView = AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50)
        banner_containerp.removeView(adView)
        banner_containerp.addView(adView)
        adView.loadAd()
    }

    override fun onDestroy() {
        if (adView != null) {
            adView!!.destroy()
        }
        super.onDestroy()
    }




}