package id.example.crudfirebase

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log.*
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.gson.Gson
import id.example.crudfirebase.models.Book
import id.example.crudfirebase.utils.fStorage
import id.example.crudfirebase.utils.fbook
import kotlinx.android.synthetic.main.activity_book.*
import java.io.File
import java.util.*

class BookActivity : AppCompatActivity() {

    private lateinit var book: Book
    private var isEdit: Boolean = false
    private var images: MutableList<Image> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isEdit = intent.getBooleanExtra("isEdit", false)

        btn_save.setOnClickListener {
            if (isEdit) update() else save()
        }

        if (isEdit) receiveBook()

        img_upload.setOnClickListener {

            img_layout.removeAllViews()

            ImagePicker
                .create(this)
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .toolbarArrowColor(Color.BLACK)
                .multi()
                .limit(5)
                .showCamera(true)
                .enableLog(false)
                .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images.clear()
            img_layout.removeAllViews()
            images.addAll(ImagePicker.getImages(data))

            images.mapIndexed { index, image ->

                val imageView = ImageView(this)
                val layout = LinearLayout.LayoutParams(190, 160)
                layout.weight = 1.0F
                layout.setMargins(1,1,1,1)
                imageView.layoutParams = layout
                Glide.with(this).load(images[index].path).into(imageView)
                img_layout.addView(imageView)

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImg(): MutableList<Uri> {

        val imgPath: MutableList<Uri> = mutableListOf()

        images.forEach {
            val path= Uri.fromFile(File(it.path))

            fStorage
                .child(UUID.randomUUID().toString())
                .putFile(path)
                .addOnSuccessListener { snapShot ->

                    snapShot
                        .storage
                        .downloadUrl
                        .addOnSuccessListener { url -> imgPath.add(url) }

                    i("UPLOAD1", "Success upload ! ${imgPath}")

                }
                .removeOnFailureListener { e("UPLOAD", "Error upload ! ${it.message}") }
        }

        images.clear() //clear old images
        img_layout.removeAllViews() //remove all ImageView

        createTV()
        return imgPath

    }

    private fun receiveBook() {
        book = Gson().fromJson(intent.getStringExtra("book"), Book::class.java)

        book.let {
            txt_name.setText(it.name)
            txt_writer.setText(it.writer)
        }
    }

    private fun save(){
        val path = uploadImg()

        e("AHAHAH", "${path}")

        val book = Book().apply {
            name = txt_name.text.toString()
            writer = txt_writer.text.toString()
            img_path = path
        }

        fbook.add(book)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                e("Error Save", "Sorry bro gagagl ! ${it.message}")
            }
    }

    private fun update() {
        book.let {
            it.name = txt_name.text.toString()
            it.writer = txt_writer.text.toString()
        }

        fbook
            .document(book.id.toString())
            .set(book)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil dirubah", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                e("Error Save", "Sorry bro gagagl ! ${it.message}")
            }
    }

    private fun createTV() {
        val textView = TextView(this)
        val layout = LinearLayout
            .LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        textView.gravity = Gravity.CENTER
        textView.layoutParams = layout
        textView.text = getString(R.string.click_to_upload)
        img_layout.addView(textView)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
