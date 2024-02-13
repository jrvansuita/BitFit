package com.vansuita.bitfit.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vansuita.bitfit.data.datasource.database.model.Record
import com.vansuita.bitfit.databinding.FragmentRecordsBinding
import com.vansuita.bitfit.extension.asDate
import java.util.Date

class RecordsFragment : Fragment() {
	private val viewModel: RecordsViewModel by viewModels { RecordsViewModel.Factory }

	private val binding by lazy {
		FragmentRecordsBinding.inflate(layoutInflater)
	}

	private val headerBinding by lazy {
		binding.headerForm
	}

	private val adapter by lazy {
		ListAdapter(::onDeleteRecord)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	) = binding.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupListeners()
		setupViews()
	}

	private fun setupViews() {
		viewModel.records.observe(viewLifecycleOwner) {
			adapter.setup(it)
			refreshData(it)
		}

		binding.recyclerView.adapter = adapter
	}

	private fun setupListeners() {
		headerBinding.save.setOnClickListener {
			if (isAnyFieldUnset())
				return@setOnClickListener

			viewModel.insert(
				feeling = headerBinding.feeling.progress,
				hours = headerBinding.hours.progress,
				notes = headerBinding.notes.text.toString()
			)
		}
	}


	private fun isAnyFieldUnset(): Boolean = headerBinding.run {
		return notes.text.isBlank() || hours.progress == 0 || feeling.progress == 0
	}

	private fun refreshData(records: List<Record>) {
		headerBinding.notes.text.clear()
		headerBinding.hours.progress = 0
		headerBinding.feeling.progress = 0

		headerBinding.root.isVisible = records.any {
			it.date.asDate() == Date().asDate()
		}.not()
	}


	private fun onDeleteRecord(record: Record) {
		viewModel.delete(record)
	}

}