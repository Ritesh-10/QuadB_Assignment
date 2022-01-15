package com.example.quadb_assignment.ui
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.quadb_assignment.data.Movie
import com.example.quadb_assignment.data.MovieDetailRepository

class MovieDetailViewModel(id: Long, application: Application): ViewModel(){
    private val repo: MovieDetailRepository =
        MovieDetailRepository(application)

    val movie: LiveData<Movie> =
        repo.getMovie(id)
}