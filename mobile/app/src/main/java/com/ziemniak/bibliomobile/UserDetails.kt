package com.ziemniak.bibliomobile


import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.ziemniak.bibliomobile.repositories.LoginResponseListener
import okhttp3.*
import okio.BufferedSink
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.Exception

object UserDetails {
	var username: String? = null
	var password: String? = null
	var jwt: String? = null
	val listeners: MutableCollection<LoginResponseListener> = mutableListOf()
	fun addListener(listener: LoginResponseListener) = listeners.add(listener)

	fun updateJwt() {
		var body = RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"), Gson().toJson(Credentials(username, password))
		)
		Log.i("AAAA", Gson().toJson(Credentials(username, password)))


		var request = Request.Builder().url(Variables.urlToServer + "auth/login").post(body).build()
		try {
			var response = OkHttpClient().newCall(request).enqueue(FetchingJwtCallback())
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	class Credentials(var username: String?, var password: String?)

}

class FetchingJwtCallback : Callback {
	override fun onFailure(call: Call, e: IOException) {
		Log.e("AAAA", "IOException while fetching jwt: ${e.message}")
	}

	override fun onResponse(call: Call, response: Response) {
		if (response.isSuccessful) {
			UserDetails.jwt = JSONObject(response.body()?.string())["jwt"] as String?
			Log.i("AAAA", "Jwt recived ${UserDetails.jwt}")
		} else {
			Log.e("AAAA", "Error while fetching jwt: ${response.body()?.string()}")
		}
		for (listener in UserDetails.listeners) {
			listener.onAction(response.isSuccessful)
		}
	}

}