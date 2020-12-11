package com.mrsoftit.kishorkontho

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_single_item_.view.*


class BookAdapter(options: FirestoreRecyclerOptions<BookNote>) :
    FirestoreRecyclerAdapter<BookNote, BookAdapter.BookViewHolder>(options) {

    companion object {
        var mClickListener: ItemClickListener? = null
    }


    interface ItemClickListener
    {
        fun clickRow(position: Int)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_single_item_,parent,false))

    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: BookNote) {

        holder.name1.setText(model.name)
        Picasso.get().load(model.imageURL).into(holder.image)

        holder.itemView.setOnClickListener { view ->
            mClickListener?.clickRow(position)
            holder.name1.setTextColor(Color.GREEN)
            Toast.makeText(view.getContext(), model.name+"", Toast.LENGTH_SHORT).show()

            val intent = Intent(view.context,PDF::class.java)
                .putExtra("bookPDF",model.bookURL)
                 view.context.startActivity(intent)
        }

    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image = itemView.book_image
        var name1 = itemView.book_name

        init {
            itemView.setOnClickListener{

               }
            }

    }
}