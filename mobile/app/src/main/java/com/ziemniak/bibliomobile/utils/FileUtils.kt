package com.ziemniak.bibliomobile.utils

object FileUtils {
	fun getExtensionName(fileName: String): String {
		val index = fileName.lastIndexOf('.')
		if (index == -1) {
			return "Plik"
		}
		if (index == 0) {
			return "Plik ukryty bez rozszerzenia"
		}
		val extension = fileName.substring(index + 1)
		return when (extension) {
			"png" -> "Obrazek png"
			"pdf" -> "Dokument pdf"
			"txt" -> "Dokument tekstowy"
			else -> "Plik $extension"
		}
	}

	fun getFileName(fileName: String): String {
		val index = fileName.lastIndexOf('.')
		if (index == -1 || index == 0) {
			return fileName
		}
		return fileName.substring(0,index)
	}

}