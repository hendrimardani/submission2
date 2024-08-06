package com.example.mysubmission2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysubmission2.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY id ASC")
    fun getEventUpComing(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event ORDER BY id ASC")
    fun getEventFinished(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event where bookmarked = 1")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUpcoming(event: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFinished(event: List<EventEntity>)

//    @Update
//    fun updateEvent(event: EventEntity)

    @Query("UPDATE event SET bookmarked = :newBookmarked WHERE id = :id")
    fun updateBookmarkEvent(id: String, newBookmarked: Boolean)

    @Query("DELETE FROM event WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND bookmarked = 1)")
    fun isEventBookmarked(id: String): Boolean
}