package com.ziemniak.bibliomobile.ui.bibliographies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BibliographyViewModel : ViewModel() {

	private val _text = MutableLiveData<String>().apply {
		value = "This is dashboard Fragment"
	}
	val text: LiveData<String> = _text
}