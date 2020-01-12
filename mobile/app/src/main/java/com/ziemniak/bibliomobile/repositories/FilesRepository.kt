package com.ziemniak.bibliomobile.repositories

import android.util.JsonReader
import com.ziemniak.bibliomobile.Variables
import com.ziemniak.bibliomobile.models.File
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object FilesRepository {
	val variables = Variables

	fun getAllFiles(): Collection<File> {

		return listOf(File(1, "aaa"))
	}

}
