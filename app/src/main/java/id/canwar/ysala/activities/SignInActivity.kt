package id.canwar.ysala.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.canwar.ysala.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initUI()
    }

    private fun initUI() {
        tv_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_sign_in.setOnClickListener{
            val email = et_email.text.toString()
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

            if (!isError) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

}