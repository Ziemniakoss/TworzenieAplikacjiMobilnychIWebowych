package com.ziemniak.bibliomobile.ui.files

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziemniak.bibliomobile.models.File
import com.ziemniak.bibliomobile.repositories.FilesRepository
import com.ziemniak.bibliomobile.repositories.FilesRepositoryEventListener

class FilesViewModel : ViewModel(), FilesRepositoryEventListener {
	init {
		FilesRepository.addListener(this)
		FilesRepository.getAllFiles()

	}

	private val _text = MutableLiveData<String>().apply {
		value = "This is home Fragment"
	}
	val text: LiveData<String> = _text

	override fun filesFetched(files: Collection<File>) {
		Log.i("AAAA", files.toString())
		println("Haaaaaaaaaaaaaaa" + files)
	}

	override fun fileFetched(file: File) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

	}
}