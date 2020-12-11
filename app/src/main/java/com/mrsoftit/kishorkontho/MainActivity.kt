package com.mrsoftit.kishorkontho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mrsoftit.kishorkontho.BookAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val db:FirebaseFirestore = FirebaseFirestore.getInstance();
    private val collectionReference: CollectionReference = db.collection("Books")

    private var bookAdapter: BookAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        setUpRecyclerView()

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
}