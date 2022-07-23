package com.positivehc.reminder.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventDbModel: EventDbModel)

    @Delete
    suspend fun delete(eventDbModel: EventDbModel)

    @Query("select * from event where id = :id")
    suspend fun get(id: Int): EventDbModel

    @Query("select * from event")
    fun getAll(): LiveData<List<EventDbModel>>

    @Query("select * from event where date(eventDateTime) = :date order by eventDateTime")
    suspend fun getByDate(date: LocalDate): List<EventDbModel>
}