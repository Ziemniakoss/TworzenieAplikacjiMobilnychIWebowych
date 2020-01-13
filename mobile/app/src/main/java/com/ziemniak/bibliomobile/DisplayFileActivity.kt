package com.ziemniak.bibliomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ziemniak.bibliomobile.utils.FileUtils
import kotlinx.android.synthetic.main.activity_display_file.*

class DisplayFileActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_display_file)
		txtvFileName.text = FileUtils.getFileName(intent.getStringExtra("name"))
		txtvFileType.text = FileUtils.getExtensionName(intent.getStringExtra("name"))
		btnDownload.setOnClickListener {
			val id = intent.getIntExtra("id",0)
			Toast.makeText(this,
				"Tymczasowo niedostępne, plik dostępny pod ${Variables.urlToServer}files/$id",Toast.LENGTH_SHORT).show()
		}

	}
}
