package com.ziemniak.bibliomobile.ui.bibliographies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ziemniak.bibliomobile.R

class BibliographyFragment : Fragment() {

	private lateinit var bibliographyViewModel: BibliographyViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		bibliographyViewModel =
			ViewModelProviders.of(this).get(BibliographyViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_bibliographies, container, false)
		val textView: TextView = root.findViewById(R.id.text_dashboard)
		bibliographyViewModel.text.observe(this, Observer {
			textView.text = it
		})
		return root
	}
}