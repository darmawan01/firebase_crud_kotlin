package id.example.crudfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.widget.Toast
import id.example.crudfirebase.models.Book
import id.example.crudfirebase.utils.fbook
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        btn_save.setOnClickListener {
            save()
        }

    }

    private fun save(){
        val book = Book().apply {
            name = txt_name.text.toString()
            writer = txt_writer.text.toString()
        }

        fbook.add(book)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                e("Error Save", "Sorry bro gagagl ! ${it.message}")
            }
    }
}
