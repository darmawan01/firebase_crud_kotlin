package id.example.crudfirebase.utils

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

val fsettings = FirebaseFirestoreSettings.Builder()
    .setPersistenceEnabled(true)
    .build()

@SuppressLint("StaticFieldLeak")
val fLibrary = FirebaseFirestore.getInstance()

//data
val fColl = fLibrary.collection("lbrary")
val fdocument = fColl.document("data")
val fbook = fdocument.collection("book")
val fcustomer = fdocument.collection("customer")

//storage
val fStorage = FirebaseStorage.getInstance().getReference("photos")

//auth
val fauth = FirebaseAuth.getInstance()



