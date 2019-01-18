package id.example.crudfirebase.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import id.example.crudfirebase.R
import id.example.crudfirebase.models.Book

class BookAdapter(private val books: MutableList<Book>, private val listener: (Book) -> Unit): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rc_book_list, parent, false))

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bindItem(books[position], listener) }

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.v_name)
        private val writer: TextView = view.findViewById(R.id.v_writer)

        fun bindItem(book: Book, listener: (Book) -> Unit) {
            name.text = book.name
            writer.text = book.writer

            itemView.setOnClickListener { listener(book) }
        }
    }
}