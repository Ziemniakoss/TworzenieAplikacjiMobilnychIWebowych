package com.ziemniak.bibliomobile.repositories

import android.util.JsonReader
import android.util.Log
import com.google.gson.Gson
import com.ziemniak.bibliomobile.UserDetails
import com.ziemniak.bibliomobile.Variables
import com.ziemniak.bibliomobile.models.File
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object FilesRepository {
	val variables = Variables
	private val listeners = mutableListOf<FilesRepositoryEventListener>()

	fun addListener(listener: FilesRepositoryEventListener) = listeners.add(listener)

	fun removeListener(listener: FilesRepositoryEventListener) = listeners.remove(listener)

	fun getAllFiles() {
		val request = Request.Builder().url("${Variables.urlToServer}files/getall")
			.addHeader("Authorization", "Bearer ${Variables.jwt}")
			.addHeader("Content-Type", "application/json").build()
		Log.i("AAAA", "Sending async call for all files")
		OkHttpClient().newCall(request).enqueue(GetAllFilesCallback())
		Log.i("AAAA","Bearer ${Variables.jwt}")

	}

	class GetAllFilesCallback : Callback {
		override fun onFailure(call: Call, e: IOException) {
			Log.e("AAAA", "Error while fetching all files: ${e.message}")
		}

		override fun onResponse(call: Call, response: Response) {
			if (response.isSuccessful) {
				Log.i("AAAA", "Fetching files was successfull")
				val jsonArray = JSONArray(response.body()?.string())
				val filesArray = mutableListOf<File>()

				for (i in 0 until jsonArray.length() - 1) {
					val json = jsonArray.getJSONObject(i)
					filesArray.add(File(json["id"] as Int, json["name"] as String))
				}
				for (listener in listeners) {
					listener.filesFetched(filesArray)
				}
			} else{
				Log.e("AAAA","Unknown error occured(${response.code()}): ${response.body()?.string()}")
			}
		}
	}
}


