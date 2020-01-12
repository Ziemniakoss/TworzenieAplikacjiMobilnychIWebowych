package com.ziemniak.bibliomobile.ui.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ziemniak.bibliomobile.R

class SharedFragment : Fragment() {

	private lateinit var sharedViewModel: SharedViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		sharedViewModel =
			ViewModelProviders.of(this).get(SharedViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_shared, container, false)
		val textView: TextView = root.findViewById(R.id.text_notifications)
		sharedViewModel.text.observe(this, Observer {
			textView.text = it
		})
		return root
	}
}