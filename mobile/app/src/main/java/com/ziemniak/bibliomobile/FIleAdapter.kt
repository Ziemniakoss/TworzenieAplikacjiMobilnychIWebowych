package com.ziemniak.bibliomobile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ziemniak.bibliomobile.models.File
import kotlinx.android.synthetic.main.file_list_item.view.*

class FIleAdapter(val context: Context, val files: List<File>) :
	RecyclerView.Adapter<FIleAdapter.FileViewHolder>() {
	inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var currentFile: File? = null
		var currentPosition = 0

		init {
			itemView.setOnClickListener {
				Toast.makeText(context, currentFile!!.name, Toast.LENGTH_SHORT).show()
				val intent = Intent(context, DisplayFileActivity::class.java)
				intent.putExtra("name", currentFile!!.name)
				intent.putExtra("id",currentFile!!.id)
				context.startActivity(intent)
			}
		}

		fun setData(file: File, position: Int) {
			itemView.txvFileName.text = file.name
			currentFile = file
			currentPosition = position
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false)
		return FileViewHolder(view)
	}

	override fun getItemCount(): Int = files.size


	override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
		holder.setData(files[position], position)
	}
}