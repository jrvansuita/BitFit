package com.vansuita.bitfit.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.vansuita.bitfit.data.datasource.database.AppDatabase
import com.vansuita.bitfit.data.datasource.database.model.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
	private val savedStateHandle: SavedStateHandle,
	private val db: AppDatabase,
) : ViewModel() {

	private val _records: MutableLiveData<List<Record>> =
		savedStateHandle.getLiveData(RECORDS_SAVED_HANDLE)
	val records: LiveData<List<Record>> = _records

	init {
		viewModelScope.launch(Dispatchers.IO) {
			//fillMockRecords()
			_records.postValue(db.recordDao().getAll())
		}
	}

	private fun fillMockRecords() {
		val dao = db.recordDao()
		repeat(3) {
			val date = Calendar.getInstance().run {
				add(Calendar.DAY_OF_YEAR, it)
				time.time
			}

			dao.insert(
				Record(
					date = date,
					note = "Mock Data",
					hours = it * 2,
					feeling = 8
				)
			)
		}
	}

	fun clearAllData() {
		viewModelScope.launch(Dispatchers.IO) {
			db.recordDao().deleteAll()
			_records.postValue(emptyList())
		}
	}

	@Suppress("UNCHECKED_CAST")
	companion object {

		private const val RECORDS_SAVED_HANDLE = "RECORDS_SAVED_HANDLE"

		val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			override fun <T : ViewModel> create(
				modelClass: Class<T>,
				extras: CreationExtras
			): T {
				val application = checkNotNull(extras[APPLICATION_KEY])
				val savedStateHandle = extras.createSavedStateHandle()

				return DashboardViewModel(
					savedStateHandle,
					AppDatabase.getInstance(application)!!
				) as T
			}
		}
	}
}