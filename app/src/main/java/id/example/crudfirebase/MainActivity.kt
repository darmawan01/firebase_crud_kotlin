package id.example.crudfirebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.*
import id.example.crudfirebase.adapter.BookAdapter
import id.example.crudfirebase.models.Book
import id.example.crudfirebase.utils.fbook
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var bookAdapter: BookAdapter? = null
    private var books: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setRecycler()

        loadBook()

        btn_book.setOnClickListener {
            startActivity(Intent(this, BookActivity::class.java))
        }

        refresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        if (refresh.isRefreshing){
            Handler().postDelayed({
                books.clear()
                loadBook()
                refresh.isRefreshing = false
            }, 500)
        }

    }

    private fun setRecycler() {
        bookAdapter = BookAdapter(books){}

        rc_book.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun loadBook() {
        fbook.get()
            .addOnSuccessListener { q ->
                books = q.toObjects(Book::class.java)

                setRecycler()
                refresh.isRefreshing = false
            }
            .addOnFailureListener {
                e("DataError", "${it.message}")
            }
    }
}