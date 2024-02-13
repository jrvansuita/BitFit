package com.vansuita.bitfit.ui.list

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
import java.util.Date

class RecordsViewModel(
	private val savedStateHandle: SavedStateHandle,
	private val db: AppDatabase,
) : ViewModel() {

	private val _records: MutableLiveData<List<Record>> =
		savedStateHandle.getLiveData(RECORDS_SAVED_HANDLE)
	val records: LiveData<List<Record>> = _records

	init {
		viewModelScope.launch(Dispatchers.IO) {
			_records.postValue(db.recordDao().getAll())
		}
	}

	fun insert(feeling: Int, hours: Int, notes: String) {
		viewModelScope.launch(Dispatchers.IO) {
			db.recordDao().insert(
				Record(
					date = Date().time,
					note = notes,
					hours = hours,
					feeling = feeling
				)
			)
			_records.postValue(db.recordDao().getAll())
		}
	}

	fun delete(record: Record) {
		viewModelScope.launch(Dispatchers.IO) {
			db.recordDao().delete(record)
			_records.postValue(db.recordDao().getAll())
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

				return RecordsViewModel(
					savedStateHandle,
					AppDatabase.getInstance(application)!!
				) as T
			}
		}
	}
}