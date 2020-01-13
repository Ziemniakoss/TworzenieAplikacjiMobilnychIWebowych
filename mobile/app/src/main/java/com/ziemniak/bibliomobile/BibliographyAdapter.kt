package com.ziemniak.bibliomobile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziemniak.bibliomobile.models.Bibliography
import kotlinx.android.synthetic.main.bibliography_list_item.view.*

class BibliographyAdapter(val context: Context, val bibliographies: List<Bibliography>) :
	RecyclerView.Adapter<BibliographyAdapter.BibliographyViewHolder>() {
	inner class BibliographyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var currentBibliography: Bibliography? = null
		var currentPostion = 0

		init {
			itemView.setOnClickListener {
				val intent = Intent(context, DisplayBibliography::class.java)
				intent.putExtra("name", currentBibliography!!.name)
				intent.putExtra("id", currentBibliography!!.id)
				context.startActivity(intent)
			}
		}

		fun setData(bilio: Bibliography, position: Int) {
			currentBibliography = bilio
			currentPostion = position
			itemView.txvBibliographyName.text = bilio.name
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibliographyViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.bibliography_list_item, parent, false)
		return BibliographyViewHolder(view)
	}

	override fun getItemCount(): Int = bibliographies.size

	override fun onBindViewHolder(holder: BibliographyViewHolder, position: Int) {
		holder.setData(bibliographies[position], position)
	}
}