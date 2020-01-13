package com.ziemniak.bibliomobile.repositories

import android.util.Log
import com.ziemniak.bibliomobile.Variables
import com.ziemniak.bibliomobile.models.Bibliography
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

object BibliographyRepository {
	val listeners: MutableCollection<BibliographyRepositoryEventListener> = mutableListOf()


	fun getAllBibliographies() {
		val request = Request.Builder()
			.url("${Variables.urlToServer}bibliography/getall")
			.addHeader("Authorization", "Bearer ${Variables.jwt}")
			.addHeader("Content-Type", "application/json").build()
		Log.i("AAAA", "Sending async request for all bibliographies")
		OkHttpClient().newCall(request).enqueue(GetAllCallback())
	}

	class GetAllCallback : Callback {
		override fun onFailure(call: Call, e: IOException) {
			Log.e("AAAA", "Error while fetching all files: ${e.message}")
		}

		override fun onResponse(call: Call, response: Response) {
			if (response.isSuccessful) {
				val jsonArray = JSONArray(response.body()?.string())
				val bibliographies = mutableListOf<Bibliography>()
				for (i in 0 until jsonArray.length() ) {
					val json = jsonArray.getJSONObject(i)
					bibliographies.add(Bibliography(json["id"] as Int, json["name"]as String))
				}
				for (listener in listeners){
					listener.bibliographiesFetched(bibliographies)
				}
				Log.i("AAAA","Successfully fetched ${bibliographies.size} bibliographies")
			} else {
				Log.e("AAAA", "Unknown error occured(${response.code()}): ${response.body()?.string()}")
			}
		}
	}

	fun getBibliography(id: Int) {

	}

}