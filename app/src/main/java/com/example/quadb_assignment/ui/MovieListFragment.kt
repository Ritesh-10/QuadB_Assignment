@file:Suppress("DEPRECATION")

package com.example.quadb_assignment.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.quadb_assignment.data.network1.ErrorCode
import com.example.quadb_assignment.data.network1.Status
import com.example.quadb_assignment.R

import kotlinx.android.synthetic.main.fragment_movie_list.*

@Suppress("DEPRECATION")
class MovieListFragment : Fragment() {

    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)
            .get(MovieListViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(movie_list) {
            adapter = MovieAdapter {
                findNavController().navigate(
                    MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(it)
                )
            }
        }

        viewModel.movies.observe(viewLifecycleOwner, Observer {
            (movie_list.adapter as MovieAdapter).submitList(it)
            if (it.isEmpty()) {
                viewModel.fetchFromNetwork()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loadingStatus ->
            when {
                loadingStatus?.status == Status.LOADING -> {
                    loading_status.visibility = View.VISIBLE
                    status_error.visibility = View.INVISIBLE
                }
                loadingStatus?.status == Status.SUCCESS -> {
                    loading_status.visibility = View.INVISIBLE
                    status_error.visibility = View.INVISIBLE
                }
                loadingStatus?.status == Status.ERROR -> {
                    loading_status.visibility = View.INVISIBLE
                    showErrorMessage(loadingStatus.errorCode, loadingStatus.message)
                    status_error.visibility = View.VISIBLE
                }
            }
            swipe_refresh.isRefreshing = false
        })
        swipe_refresh.setOnRefreshListener{
            viewModel.refreshData()
        }
    }
private fun showErrorMessage(errorCode: ErrorCode?, message: String?) {
    when (errorCode) {
        ErrorCode.NO_DATA -> status_error.text =getString(R.string.no_data)
        ErrorCode.NETWORK_ERROR -> status_error.text = getString(R.string.network_error)
        ErrorCode.UNKNOWN_ERROR -> status_error.text = getString(R.string.unknowing)
    }
}
}

