package id.example.crudfirebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.*
import android.view.*
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import id.example.crudfirebase.adapter.BookAdapter
import id.example.crudfirebase.models.Book
import id.example.crudfirebase.utils.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var bookAdapter: BookAdapter? = null
    private var books: MutableList<Book> = mutableListOf()

    private var isLoading: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setRecycler()

        loadBook()

        btn_book.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
                .putExtra("isEdit", false)

            startActivity(intent)
        }

        refresh.setOnRefreshListener(this)
    }

    private fun loadBook() {
        if (isLoading) loading.visibility = View.VISIBLE

        fbook
            .get()
            .addOnSuccessListener { q ->
                q.map { snapshot ->

                    val data = snapshot.toObject(Book::class.java)
                    data.id = snapshot.id

                    books.add(data)
                }

                e("DataError", "${books}")

                setRecycler()
                refresh.isRefreshing = false
                loading.visibility = View.GONE

            }
            .addOnFailureListener {
                e("DataError", "${it.message}")
            }
    }

    private fun setRecycler() {
        bookAdapter = BookAdapter(books){ q ->

            val book = Gson().toJson(q)

            val intent = Intent(this, BookActivity::class.java)
                .putExtra("book", book)
                .putExtra("isEdit", true)

            startActivity(intent)
        }
        bookAdapter?.notifyDataSetChanged()

        rc_book.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.btn_logout -> {
                fauth.signOut()
                clearPref()
                Toast.makeText(this, "Logout Success, See you !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        if (refresh.isRefreshing){
            isLoading = false
            books.clear()
            loadBook()
        }

    }
}