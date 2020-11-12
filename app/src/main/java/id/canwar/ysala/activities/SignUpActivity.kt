package id.canwar.ysala.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.canwar.ysala.R
import id.canwar.ysala.helpers.FIREBASE_USERS
import id.canwar.ysala.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null)
            startActivity(Intent(this, DashboardActivity::class.java))
    }

    private fun initUI() {

        btn_sign_up.setOnClickListener {

            val fullName = et_full_name.text.toString()
            val email = et_email.text.toString()
            val phone = et_phone_number.text.toString()
            val password = et_password.text.toString()

            var isError = false

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.error = resources.getString(R.string.invalid_email)
                et_email.isFocusable = true
                isError = true
            }

            if (password.length < 8) {
                et_password.error = resources.getString(R.string.invalid_password)
                et_password.isFocusable = true
                isError = true
            }

            if (fullName.length < 3) {
                et_full_name.error = resources.getString(R.string.invalid_full_name)
                et_full_name.isFocusable = true
                isError = true
            }

            if (!isError) {
                firebaseAddUser(fullName, email, phone, password)
            }
        }

        tv_sign_in.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun firebaseAddUser(fullName: String, email: String, phone: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val uid = firebaseAuth.uid!!

                        val user = User(uid, fullName, email, phone)

                        val databaseReference = firebaseDatabase.getReference(FIREBASE_USERS)

                        databaseReference.child(uid).setValue(user)

                        Toast.makeText(baseContext, resources.getString(R.string.success_sign_up), Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }

    }

}