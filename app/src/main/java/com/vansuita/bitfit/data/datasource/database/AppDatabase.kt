package com.vansuita.bitfit.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vansuita.bitfit.data.datasource.database.dao.RecordDao
import com.vansuita.bitfit.data.datasource.database.model.Record

@Database(entities = [Record::class], version = 1)
abstract class AppDatabase2 : RoomDatabase() {
	abstract fun recordDao(): RecordDao
}

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

	abstract fun recordDao(): RecordDao

	companion object {
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase? {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class) {
					INSTANCE = Room.databaseBuilder(
						context.applicationContext,
						AppDatabase::class.java, "record.db"
					)
						//.allowMainThreadQueries()
						.build()
				}
			}
			return INSTANCE
		}

		fun destroyInstance() {
			INSTANCE = null
		}
	}
}