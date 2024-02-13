package com.vansuita.bitfit.data.datasource.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
	@PrimaryKey @ColumnInfo(name = "date") val date: Long,
	@ColumnInfo(name = "note") val note: String,
	@ColumnInfo(name = "hours") val hours: Int,
	@ColumnInfo(name = "feeling") val feeling: Int,
)