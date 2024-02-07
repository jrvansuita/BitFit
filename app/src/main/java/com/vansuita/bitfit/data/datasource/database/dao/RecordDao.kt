package com.vansuita.bitfit.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vansuita.bitfit.data.datasource.database.model.Record

@Dao
interface RecordDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(record: Record)

	@Delete
	fun delete(record: Record)

	@Query("SELECT * FROM record")
	fun getAll(): List<Record>
}