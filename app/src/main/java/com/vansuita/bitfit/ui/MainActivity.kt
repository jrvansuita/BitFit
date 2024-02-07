package com.vansuita.bitfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.vansuita.bitfit.R
import com.vansuita.bitfit.data.datasource.database.AppDatabase
import com.vansuita.bitfit.data.datasource.database.model.Record
import com.vansuita.bitfit.databinding.ActivityMainBinding
import com.vansuita.bitfit.extension.asDate
import java.util.Date

class MainActivity : AppCompatActivity() {

	private val binding by lazy {
		ActivityMainBinding.inflate(layoutInflater)
	}

	private val headerBinding by lazy {
		binding.headerForm
	}

	private val metricsBinding by lazy {
		binding.metrics
	}

	private val db by lazy {
		AppDatabase.getInstance(this)?.recordDao()!!
	}

	private val adapter by lazy {
		ListAdapter(::onDeleteRecord)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbar)
		setupListeners()
		setupViews()
		refreshData()
	}

	private fun setupViews() {
		val records = db.getAll()
		adapter.add(*records.toTypedArray())
		binding.recyclerView.adapter = adapter
	}


	private fun setupListeners() {
		headerBinding.save.setOnClickListener {
			if (headerBinding.notes.text.isBlank())
				return@setOnClickListener
			if (headerBinding.hours.progress == 0)
				return@setOnClickListener
			if (headerBinding.feeling.progress == 0)
				return@setOnClickListener

			val record = Record(
				date = Date().time,
				note = headerBinding.notes.text.toString(),
				hours = headerBinding.hours.progress,
				felling = headerBinding.feeling.progress
			)
			db.insert(record)
			adapter.add(record)
			refreshData()
		}
	}

	private fun refreshData() {
		val records = db.getAll()

		var hasRecordForToday = false
		var averageHoursOfSleep = 0
		var averageFeeling = 0


		if (records.isNotEmpty()) {
			var sumHoursOfSleep = 0
			var sumFeeling = 0
			records.forEach {
				hasRecordForToday = hasRecordForToday || it.date.asDate() == Date().asDate()
				sumHoursOfSleep += it.hours
				sumFeeling += it.felling
			}

			averageHoursOfSleep = sumHoursOfSleep / records.size
			averageFeeling = sumFeeling / records.size
		}

		metricsBinding.metricsHours.text =
			getString(R.string.average_hours_of_sleep, averageHoursOfSleep)
		metricsBinding.metricsFeeling.text =
			getString(R.string.average_feeling, averageFeeling)


		headerBinding.notes.text.clear()
		headerBinding.hours.progress = 0
		headerBinding.feeling.progress = 0

		headerBinding.root.isVisible = !hasRecordForToday
	}


	private fun onDeleteRecord(record: Record) {
		db.delete(record)
		adapter.delete(record)
		refreshData()
	}
}