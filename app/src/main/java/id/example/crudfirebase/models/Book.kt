package id.example.crudfirebase.models

data class Book (
    var id: String? = null,
    var name: String? = null,
    var writer: String? = null,
    var img_path: MutableList<String>? = null
)