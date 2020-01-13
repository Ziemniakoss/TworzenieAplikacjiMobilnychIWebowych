package com.ziemniak.bibliomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main_menu)
		btnShowFiles.setOnClickListener {
			val intent = Intent(this, FileMenuActivity::class.java)
			startActivity(intent)
		}
		btnShowBibliographies.setOnClickListener {
			val intent = Intent(this, BibliographyMenuActivity::class.java)
			startActivity(intent)
		}
	}
}
