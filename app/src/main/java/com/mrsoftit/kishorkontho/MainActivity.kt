package com.mrsoftit.kishorkontho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mrsoftit.kishorkontho.BookAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_p_d_f.*


class MainActivity : AppCompatActivity() {

    private val db:FirebaseFirestore = FirebaseFirestore.getInstance();
    private val collectionReference: CollectionReference = db.collection("Books")

    private var bookAdapter: BookAdapter? = null

    private var interstitilAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AudienceNetworkAds.initialize(this)

        setUpRecyclerView()
        showBanner()
        showInterstitialAd()



    }

    private fun setUpRecyclerView() {
        val query:Query = collectionReference.orderBy("number",Query.Direction.DESCENDING)

        val firestoreRecyclerOptions:FirestoreRecyclerOptions<BookNote> = FirestoreRecyclerOptions.Builder<BookNote>()
            .setQuery(query,BookNote::class.java)
            .build()
        bookAdapter = BookAdapter(firestoreRecyclerOptions)

        recyclerview.layoutManager = GridLayoutManager(this,2)

        recyclerview.adapter = bookAdapter

    }

    override fun onStart() {
        super.onStart()

        bookAdapter!!.startListening()

    }

    override fun onStop() {
        super.onStop()

        bookAdapter!!.stopListening()

    }

    private fun showBanner() {
        val adView = AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50)
        banner_container.removeView(adView)
        banner_container.addView(adView)
        adView.loadAd()
    }

    private fun showInterstitialAd() {
        interstitilAd = InterstitialAd(this, getString(R.string.interstitial))
        interstitilAd!!.setAdListener(object : InterstitialAdListener {
            override fun onInterstitialDisplayed(p0: Ad?) {

            }

            override fun onError(p0: Ad?, p1: AdError?) {
            }

            override fun onAdLoaded(p0: Ad?) {
                interstitilAd!!.show()
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onInterstitialDismissed(p0: Ad?) {
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        })
        interstitilAd!!.loadAd()
    }
}