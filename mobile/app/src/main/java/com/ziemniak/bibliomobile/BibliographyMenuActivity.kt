package com.ziemniak.bibliomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziemniak.bibliomobile.models.Bibliography
import com.ziemniak.bibliomobile.repositories.BibliographyRepository
import com.ziemniak.bibliomobile.repositories.BibliographyRepositoryEventListener
import kotlinx.android.synthetic.main.activity_bibliography_menu.*

class BibliographyMenuActivity : AppCompatActivity() , BibliographyRepositoryEventListener{

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_bibliography_menu)
		BibliographyRepository.listeners.add(this)
		BibliographyRepository.getAllBibliographies()
		val layoutmanager = LinearLayoutManager(this)
		layoutmanager.orientation = LinearLayoutManager.VERTICAL
		recyclerView.layoutManager = layoutmanager
		recyclerView.adapter = BibliographyAdapter(this, listOf())
		btnAddBibliography.setOnClickListener {
			val intent = Intent(this, CreateBibliographyActivity::class.java)
			startActivity(intent)
		}
	}

	override fun bibliographyFetched(bibliography: Bibliography) {	}

	override fun bibliographiesFetched(bibliographies: List<Bibliography>) {
		runOnUiThread{
			recyclerView.swapAdapter(BibliographyAdapter(this, bibliographies),false)
		}
	}
}
