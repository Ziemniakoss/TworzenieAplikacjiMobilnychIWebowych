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

	fun hek(jwtNew: String) {
		jwt = jwtNew
	}

	fun updateJwt(): Call? {
		val credentials = Credentials(username, password)
		Log.i("AAAA", "Kurwa no ${credentials.password} ${credentials.username}")


		Log.i("AAAA", "Czy ti ti" + Gson().toJson(credentials))

		val body = RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"), Gson().toJson(credentials)
		)
		var request = Request.Builder().url(Variables.urlToServer + "auth/login").post(body).build()
		val call = OkHttpClient().newCall(request)
		call.enqueue(FetchingJwtCallback())
		return call
	}

	class FetchingJwtCallback : Callback {
		override fun onFailure(call: Call, e: IOException) {
			Log.e("AAAA", "IOException while fetching jwt: ${e.message}")
		}

		override fun onResponse(call: Call, response: Response) {
			if (response.isSuccessful) {
				val hek:String = JSONObject(response.body()?.string())["jwt"] as String
				Log.i("AAAA","Mamy hek = $hek")
				synchronized(this) {
					jwt = hek
					println("Już $jwt")
				}

				Log.i("AAAA", "Jwt recived $jwt")
			} else {
				Log.e("AAAA", "Error while fetching jwt: ${response.body()?.string()}")
			}
			for (listener in UserDetails.listeners) {
				listener.onAction(response.isSuccessful, jwt)
			}
		}
	}
}

/**
 * Na potrzeby jsonowania heheheh dajcie mi spac
 */
class Credentials(val username: String?, val password: String?)
