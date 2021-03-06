package com.ganesh.divekar.localdata.db

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ganesh.divekar.localdata.models.ComicsLocal
import com.ganesh.divekar.localdata.utils.StringConverter

@Database(
    entities = [ComicsLocal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringConverter::class)
abstract class ComicsDB : RoomDatabase() {

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "comics.db"
        @Volatile
        private var INSTANCE: ComicsDB? = null

        fun getInstance(@NonNull context: Context): ComicsDB {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            ComicsDB::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getComicsListDao(): ComicsListDAO


}