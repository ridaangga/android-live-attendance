package com.example.liveattendanceapp.hawkstorage

import android.content.Context
import com.example.liveattendanceapp.model.User
import com.orhanobut.hawk.Hawk

class HawkStorage {
    companion object{
        private const val USER_KEY = "user_key"
        private const val TOKEN_KEY = "token_key"
        private val hawkStorage = HawkStorage()

        //nanti untuk memanggil class ini, misalnya di loginActivity atau lainnya,
        //tinggal tambahkan HawkStorage.instance(this)
        fun instance(context: Context?): HawkStorage{
            Hawk.init(context).build()
            return hawkStorage
        }
    }

    fun setUser(user: User){
        Hawk.put(USER_KEY, user)
    }

    fun getUser(): User{
        return Hawk.get(USER_KEY)
    }

    fun setToken(token: String){
        Hawk.put(TOKEN_KEY, token)
    }

    fun getToken(): String{
        val rawToken = Hawk.get<String>(TOKEN_KEY)
        val token = rawToken.split("|") //di token yang di postman kan ada pemisah | sedangkan yang dibutuhkan tokenya saja. jadi di split.
        //Setelah dipisahkan (split), lalu ambil aray index 1 (mulainya dari index 0).
        return token[1]
    }

    //cek apakah sudah login atau belum
    fun isLogin(): Boolean{
        if (Hawk.contains(USER_KEY)){
            return true
        }
        return false
    }

    //Ketika logout, seluruh data di hawk hapus.
    fun deleteAll(){
        Hawk.deleteAll()
    }
}