package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DateConverter.getDateFromToday
import com.udacity.asteroidradar.domain.DateConverter.getToday
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val asteroidRepository = AsteroidRepository(getDatabase(application))

    private var _pictureOfDay: LiveData<PictureOfDay> = MutableLiveData()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private var _asteroidlist: MutableLiveData<List<Asteroid>> = MutableLiveData()
    val asteroidlist: LiveData<List<Asteroid>>
        get() = _asteroidlist

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail
        get() = _navigateToDetail

    init {
        refreshDataFromRepository()
        fetchAsteroidsData()
        fetchPictureOfTheDayData()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
            } catch (_: Exception) {
                Log.i("Asteroid Radar", "refreshAsteroids failed")
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToDetail.value = null
    }

    private fun fetchAsteroidsData(startDate: String = "", endDate: String = startDate) {
        viewModelScope.launch {
            _asteroidlist.value = asteroidRepository.getAsteroids(startDate, endDate)
        }
    }

    private fun fetchPictureOfTheDayData() {
        viewModelScope.launch {
            _pictureOfDay = asteroidRepository.getPictureOfTheDay()
            Log.e("AsteroidRadar",_pictureOfDay.toString())
        }
    }

    fun onMenuItemSelected(itemId: Int) {
        when (itemId) {
            R.id.show_today_asteroids -> {
                fetchAsteroidsData(startDate = getToday())
            }
            R.id.show_week_asteroids -> {
                fetchAsteroidsData(startDate = getToday(), endDate = getDateFromToday(7))
            }
            R.id.show_saved_asteroids -> {
                fetchAsteroidsData("", "")
            }
        }
    }


}