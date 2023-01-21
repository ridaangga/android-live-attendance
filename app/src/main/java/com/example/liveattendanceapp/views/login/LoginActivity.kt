package com.example.liveattendanceapp.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.example.liveattendanceapp.R
import com.example.liveattendanceapp.databinding.ActivityLoginBinding
import com.example.liveattendanceapp.dialog.MyDialog
import com.example.liveattendanceapp.hawkstorage.HawkStorage
import com.example.liveattendanceapp.model.LoginResponse
import com.example.liveattendanceapp.networking.ApiServices
import com.example.liveattendanceapp.networking.RetrofitClient
import com.example.liveattendanceapp.views.forgotpass.ForgotPasswordActivity
import com.example.liveattendanceapp.views.main.MainActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
        binding.btnLogin.setOnClickListener {
            //startActivity<MainActivity>() ->sebelum menggunakan LoginRequest dari POJO from JSON
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            //jika email/password valid, maka jalankan function loginTOServer.
            if (isFormValid(email, password)) {
                loginToServer(email, password)
            }
        }
        binding.btnForgotPassword.setOnClickListener {
            startActivity<ForgotPasswordActivity>()
        }
    }

    //jika validasi email yang dibawah berhasil, jalankan fungsi login to server
    private fun loginToServer(email: String, password: String) {
        val loginRequest = LoginRequest(email = email, password = password, deviceName = "mobile")
        val loginRequestString = Gson().toJson(loginRequest) //Gson dari retrofit yang di build gradle
        MyDialog.showProgressDialog(this) //tampilkan progress bar.

        ApiServices.getLiveAttendanceServices()
            .loginRequest(loginRequestString) //loginRequest ini dari loginRequest LeveAttendanceApiServices.kt. Data yang dikirmkan adalah loginRequestString
            //karena loginRequest berupa call, maka menggunakan enqueue. import librarybta (alt+enter) pakai yang callback (retrofit2).
            //agar datanya sama, LoginResponse didapat dari fil LiveAttendanceServices.kt juga. dan sesuai juga dengan urutan yang ada di class LoginResponse.kt.
            .enqueue(object : Callback<LoginResponse> {
                //override fun onResponse digenerate dari .enqueue(object ketika alt+enter dan pilih implement members
                override fun onResponse(
                    call: Call<LoginResponse>, //callnya gunakan yang retrofit2 (alt+enter)
                    response: Response<LoginResponse> //response gunakan yang retrofit2 (alt+enter)
                ) {
                    MyDialog.hideDialog()
                    if (response.isSuccessful){ //jika responsenya http 200/200, ambil data dibawahnya.
                        val user = response.body()?.user //ambil data user
                        val token = response.body()?.meta?.token //ambil data token.
                        if (user != null && token != null){ //jika user dan token tidak kosong
                            HawkStorage.instance(this@LoginActivity).setUser(user) //simpan data user ke hawkstorage yang sudah disiapkan sebelumnya.
                            HawkStorage.instance(this@LoginActivity).setToken(token) //simpan data token ke hawkstorage yang sudah disiapkan sebelumnya.
                            //lalu pergi ke MainActivity. Jika goToMain nya merah, alt+enter pilih create function "goToMain". habis itu pilih yang LoginActivity.
                            goToMain()
                        }
                    }else{ //respon kalo passwordnya salah
                        val errorConverter: Converter<ResponseBody, LoginResponse> =
                            RetrofitClient
                                .getClient()
                                .responseBodyConverter(
                                    LoginResponse::class.java,
                                    arrayOfNulls<Annotation>(0)
                                )
                        var errorResponse: LoginResponse? //errornya didapatkan dari loginResponse
                        try {
                            response.errorBody()?.let {
                                errorResponse = errorConverter.convert(it) //ubah ke it
                                MyDialog.dynamicDialog(
                                    this@LoginActivity,
                                    getString(R.string.failed), //hasil extract to string dari "Failed"
                                    errorResponse?.message.toString()
                                )
                            }
                        }catch (e: IOException){ //IOException pake yang java
                            e.printStackTrace()
                            Log.e(TAG, "Error: ${e.message}")
                        }
                    }
                }

                //ovveride fun onFailure digenerate dari .enqueue(object ketika alt+enter dan pilih implement members
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    MyDialog.hideDialog() //ketika error, sembunyikan progress dialog dan tampilkan errornya.
                    Log.e(TAG, "Error: ${t.message}")
                }
            })
    }

    private fun goToMain() {
        startActivity<MainActivity>() //startActivitynya pilih yang dari anko jetbrain yang sudah ada di library build gradle.
        finishAffinity() //agar ketika nanti di back bukan back ke login, tapi langsung ke menu android.
    }

    //validasi email
    private fun isFormValid(email: String, password: String): Boolean {
        if(email.isEmpty()){ //jika emailnya kosong
            binding.etEmailLogin.error = getString(R.string.please_field_your_email) //asalnya tulisan biasa, di extract string resource jadi getString(R.string.please_field_your_email).
            binding.etEmailLogin.requestFocus() //jika emailnya kosong, fokus akan menampilkan tulisan error diatas.
        //!Patterns adalah regullar expression,
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //jika emailnya tidak valid
            //email yang valid harus berupa huruf, @,dan ada akhiran dot (.).
            binding.etEmailLogin.error = getString(R.string.please_use_valid_email)
            binding.etEmailLogin.requestFocus()
        } else if (password.isEmpty()) { //jika passwordnya kosong
            binding.etEmailLogin.error = null //form emailnya di kosongkan lagi karena harus diisi.
            binding.etPasswordLogin.error = getString(R.string.please_field_your_password)
            binding.etPasswordLogin.requestFocus()
        } else{
            binding.etEmailLogin.error = null
            binding.etPasswordLogin.error = null
            return true
        }
        return false
    }

    //ambil nama LoginActivity, agar proses debugnya lebih gampang, gunakan key TAG.
    //TAG ini akan berhubungan juga dengan log.e(TAG.. di class ovveride fun onResponse dan ovveride fun onFailure di atas.
    companion object{
        private val TAG = LoginActivity::class.java.simpleName
    }
}
