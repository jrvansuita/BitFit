package com.vansuita.bitfit.ui.list


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vansuita.bitfit.R
import com.vansuita.bitfit.data.datasource.database.model.Record
import com.vansuita.bitfit.databinding.ListItemBinding
import com.vansuita.bitfit.extension.asDate


class ListAdapter(private val onLongClick: (record: Record) -> Unit) :
	RecyclerView.Adapter<ListAdapter.ViewHolder>() {

	private val data: MutableList<Record> = mutableListOf()

	inner class ViewHolder(private val binding: ListItemBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(record: Record) {
			binding.date.text = record.date.asDate()
			binding.note.text = record.note
			binding.feeling.text = binding.root.context.getString(R.string.feeling, record.feeling)
			binding.hours.text = binding.root.context.getString(R.string.hours_slept, record.hours)
			binding.root.setOnLongClickListener {
				onLongClick(record)
				true
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(data[position])
	}

	fun setup(records: List<Record>) {
		data.clear()
		data.addAll(records)
		notifyDataSetChanged()
	}
}