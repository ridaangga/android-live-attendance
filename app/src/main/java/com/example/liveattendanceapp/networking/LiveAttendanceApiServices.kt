package com.example.liveattendanceapp.networking

import com.example.liveattendanceapp.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LiveAttendanceApiServices {
    @Headers("Accept: application/json", "Content-Type: application/json") //headernya ssuaikan dengan pengecekan seperti di postman.
    @POST("auth/login") //method auth/login ini POST. seperti di postman.
    fun loginRequest(@Body body: String): Call<LoginResponse> //kirimkan bodynya sesuai dengan yang di postman, berupa string/json. LoginResponse dari model dengan plugin POJO response.
}