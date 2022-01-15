package com.example.quadb_assignment.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MovieDetailDao {
    @Query("SELECT * FROM movie WHERE `id` = :id")
    fun getMovie(id: Long): LiveData<Movie>
}