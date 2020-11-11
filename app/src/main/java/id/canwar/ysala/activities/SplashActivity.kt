package id.canwar.ysala.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.canwar.ysala.R

class SplashActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        finish()
    }

}