package com.vansuita.bitfit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.vansuita.bitfit.R
import com.vansuita.bitfit.data.datasource.database.model.Record
import com.vansuita.bitfit.databinding.FragmentDashboardBinding
import com.vansuita.bitfit.extension.asDate
import java.util.Date


class DashboardFragment : Fragment() {

	private val binding by lazy {
		FragmentDashboardBinding.inflate(layoutInflater)
	}

	private val viewModel: DashboardViewModel by viewModels { DashboardViewModel.Factory }

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	) = binding.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViews()
	}

	private fun setupChart(record: List<Record>) {
		val hours: MutableList<Entry> = ArrayList()
		val feeling: MutableList<Entry> = ArrayList()
		record.forEachIndexed { i, r ->
			hours.add(Entry(i.toFloat(), r.hours.toFloat()))
			feeling.add(Entry(i.toFloat(), r.feeling.toFloat()))
		}

		val custom4 = ContextCompat.getColor(requireContext(), R.color.custom4)
		val hoursDataSet = LineDataSet(hours, "hours")
		hoursDataSet.color = custom4
		hoursDataSet.valueTextColor = custom4

		val custom3 = ContextCompat.getColor(requireContext(), R.color.custom3)
		val feelingDataSet = LineDataSet(feeling, "feeling")
		feelingDataSet.color = custom3
		feelingDataSet.valueTextColor = custom3

		binding.chart.data = LineData(hoursDataSet, feelingDataSet)
		binding.chart.invalidate()
	}

	private fun setupViews() {
		binding.clearData.setOnClickListener {
			viewModel.clearAllData()
		}

		viewModel.records.observe(viewLifecycleOwner) {
			setupData(it)
			setupChart(it)
		}
	}

	private fun setupData(records: List<Record>) {
		var hasRecordForToday = false
		var averageHoursOfSleep = 0
		var averageFeeling = 0

		if (records.isNotEmpty()) {
			var sumHoursOfSleep = 0
			var sumFeeling = 0
			records.forEach {
				hasRecordForToday = hasRecordForToday || it.date.asDate() == Date().asDate()
				sumHoursOfSleep += it.hours
				sumFeeling += it.feeling
			}

			averageHoursOfSleep = sumHoursOfSleep / records.size
			averageFeeling = sumFeeling / records.size
		}

		binding.metricsHours.text = getString(R.string.average_hours_of_sleep, averageHoursOfSleep)
		binding.metricsFeeling.text = getString(R.string.average_feeling, averageFeeling)

	}
}

