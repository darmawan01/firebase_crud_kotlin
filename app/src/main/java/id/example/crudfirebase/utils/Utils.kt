package id.example.crudfirebase.utils

import com.google.firebase.firestore.FirebaseFirestore

val fLibrary = FirebaseFirestore.getInstance().collection("lbrary")

val fdocument = fLibrary.document("data")
val fbook = fdocument.collection("book")
val fcustomer = fdocument.collection("customer")

