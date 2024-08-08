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

    @Query("SELECT * FROM event WHERE favorite = 1")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE favorite = 1")
    fun getFavorite(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUpcoming(event: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFinished(event: List<EventEntity>)

    @Query("UPDATE event SET favorite = :newFavorite WHERE id = :id")
    fun updateFavoriteEvent(id: String, newFavorite: Boolean)

    @Query("DELETE FROM event WHERE favorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND favorite = 1)")
    fun isEventFavorite(id: String): LiveData<Boolean>
}