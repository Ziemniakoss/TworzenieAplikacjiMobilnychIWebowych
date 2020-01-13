package com.ziemniak.bibliomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziemniak.bibliomobile.models.File
import com.ziemniak.bibliomobile.repositories.FilesRepository
import com.ziemniak.bibliomobile.repositories.FilesRepositoryEventListener
import kotlinx.android.synthetic.main.activity_file_menu.*

class FileMenuActivity : AppCompatActivity(), FilesRepositoryEventListener {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_file_menu)

		FilesRepository.addListener(this)
		FilesRepository.getAllFiles()
		val layoutManager = LinearLayoutManager(this)
		layoutManager.orientation = LinearLayoutManager.VERTICAL
		recyclerView.layoutManager = layoutManager
		recyclerView.adapter = FIleAdapter(this, listOf())

	}

	override fun filesFetched(files: List<File>) {
		runOnUiThread {
			recyclerView.swapAdapter(FIleAdapter(this, files), false)
		}
	}

	override fun fileFetched(file: File) {}
}
