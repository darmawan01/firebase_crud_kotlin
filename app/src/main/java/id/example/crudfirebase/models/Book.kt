package id.example.crudfirebase.models

import android.net.Uri

data class Book (
    var id: String? = null,
    var name: String? = null,
    var writer: String? = null,
    var img_path: MutableList<Uri>? = null
)