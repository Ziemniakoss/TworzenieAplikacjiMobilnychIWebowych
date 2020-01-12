package com.ziemniak.bibliomobile.repositories

import android.util.JsonReader
import android.util.Log
import com.google.gson.Gson
import com.ziemniak.bibliomobile.UserDetails
import com.ziemniak.bibliomobile.Variables
import com.ziemniak.bibliomobile.models.File
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object FilesRepository {
	val variables = Variables
	private val listeners = mutableListOf<FilesRepositoryEventListener>()

	fun addListener(listener: FilesRepositoryEventListener) = listeners.add(listener)

	fun removeListener(listener: FilesRepositoryEventListener) = listeners.remove(listener)

	fun getAllFiles(): Collection<File> {
		val request = Request.Builder().url("${Variables.urlToServer}files/getall")
			.addHeader("Authorization", "Bearer ${Variables.jwt}").build()
		OkHttpClient().newCall(request).enqueue(GetAllFilesCallback())



		return listOf(File(1, "aaa"))
	}

	class GetAllFilesCallback : Callback {
		override fun onFailure(call: Call, e: IOException) {
			Log.e("AAAA","Error while fetching all files: ${e.message}")
		}

		override fun onResponse(call: Call, response: Response) {
			if(response.isSuccessful){
				JSONObject(response.body()?.string()).
			}else if(response.code() == 503){
				//Wygasł jwt, odnawiamy
				Log.i("AAAA","Update of jwt required before fetching files")
				val call = UserDetails.updateJwt() ?: return
				while (!call.isExecuted){
					Thread.yield()
				}
				getAllFiles()
			}
		}
	}
}


