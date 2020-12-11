package com.mrsoftit.kishorkontho

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_p_d_f.*


class PDF : AppCompatActivity() {

    var storage: FirebaseStorage = FirebaseStorage.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f)


        var bundle :Bundle ?=intent.extras
        var pdfurl = bundle!!.getString("bookPDF")

        Toast.makeText(this,pdfurl,Toast.LENGTH_SHORT).show()


        pdfurl?.let {
            storage.getReferenceFromUrl(it)
                .getBytes(900000000)
                .addOnSuccessListener { bytes ->
                    pdfView.fromBytes(bytes)
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load()

                }
                .addOnFailureListener { e -> Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show() }
        }
    }
}