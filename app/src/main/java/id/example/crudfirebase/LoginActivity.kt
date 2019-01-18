package id.example.crudfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log.*
import android.view.WindowManager
import android.widget.Toast
import id.example.crudfirebase.utils.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var isRegister: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fLibrary.firestoreSettings = fsettings

        checkAuth()

        //setup full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btn_login.setOnClickListener {
            if (isRegister) doRegister() else doLogin()
        }

        swith_type.setOnClickListener {
            switchMode()
        }
    }

    private fun checkAuth() {
        if (!email.isNullOrBlank()) {
            Toast.makeText(this, "Selamat datang kembali !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun doLogin() {
        val email: String = txt_email.text.toString()
        val password: String = txt_password.text.toString()

        fauth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!it.user.email.isNullOrBlank()) {
                    Toast.makeText(this, "Login Success !", Toast.LENGTH_SHORT).show()

                    saveToPref(it.user.email.toString(), "email")
                    startActivity(Intent(this, MainActivity::class.java))

                    finish()
                }
            }
            .addOnFailureListener {
                e("ERRAUTH", "${it.message}")
            }
    }

    private fun doRegister() {
        val email: String = txt_email.text.toString()
        val password: String = txt_password.text.toString()

        fauth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!it.user.email.isNullOrBlank()) {

                    val builder = AlertDialog.Builder(this)
                    builder
                        .setMessage("Register berhasil. Login dengan akun ${it.user.email} ?")
                        .setPositiveButton("Yes", { dialog, _ ->
                            doLogin()
                        })
                        .setNegativeButton("No", { dialog, _ ->
                            switchMode()
                            txt_email.setText(it.user.email)
                            dialog.dismiss()

                        }).show()
                }

            }
            .addOnFailureListener { e("ERRAUTH", "${it.message}") }
    }

    private fun switchMode() {

        isRegister = !isRegister

        clearEdt()

        if (isRegister) {
            swith_type.setText(getString(R.string.login_switch))
            btn_login.setText(getString(R.string.register))
        } else {
            swith_type.setText(getString(R.string.i_don_t_have_account_resgiter))
            btn_login.setText(getString(R.string.login))
        }
    }

    private fun clearEdt() {
        txt_email.setText("")
        txt_password.setText("")
    }
}
