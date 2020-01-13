package com.ziemniak.bibliomobile.ui.files

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ziemniak.bibliomobile.R
import com.ziemniak.bibliomobile.models.File
import kotlinx.android.synthetic.main.file_list_item.view.*

class FilesAdapter(val context: Context, val files: List<File>) : RecyclerView.Adapter<FilesAdapter.MyFileHolder {
	inner class MyFileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun setData(file: File, position: Int) {
			currentFile = file
			currentPosition = position
			itemView.txvFileName.text = file.name
		}
		var currentFile: File? = null
		var currentPosition = 0
		init {
			itemView.setOnClickListener {
				Toast.makeText(context,currentFile!!.name,Toast.LENGTH_SHORT).show()
			}
		}


	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFileHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false)
		return MyFileHolder(view)
	}

	override fun getItemCount(): Int = files.size

	override fun onBindViewHolder(holder: MyFileHolder, position: Int) {
		val file = files[position]
		holder.setData(file, position)
	}
}