package com.example.mysubmission2.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mysubmission2.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "Event.db"
                ).build()
        }
    }

//     Fungsi untuk menghapus database
//    companion object {
//        @Volatile
//        private var instance: EventDatabase? = null
//        fun getInstance(context: Context): EventDatabase {
//            instance ?: synchronized(this) {
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    EventDatabase::class.java, "Event2.db"
//                ).build()
//            }
//            val dbPath = context.getDatabasePath("Event2.db")
//            dbPath.delete()
//            return instance as EventDatabase
//        }
//    }
}