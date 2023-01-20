package com.example.liveattendanceapp.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.liveattendanceapp.R
import com.example.liveattendanceapp.views.login.LoginActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        afterDelayGoToLogin() //setelah masuk ke spash screen, lalu masuk ke halaman login.
    }

    private fun afterDelayGoToLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity<LoginActivity>()
            finishAffinity() //ketika masuk login activity, lalu klik back, maka akan destroy (tidak akan back ke splash, jadi tetap di halaman login)
        },1200) //delay 1,2 detik.
    }
}