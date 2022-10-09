package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.network.Api
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidsDatabase) {
   /* val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    */

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val response = Api.retrofitService.getResponse()

            val asteroidList = parseAsteroidsJsonResult(JSONObject(response))
            database.asteroidDao.insertAll(asteroidList.asDatabaseModel())
        }
    }

    suspend fun getAsteroids(startDate: String = "", endDate: String = startDate): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val asteroidList =
            if  (startDate.isNotBlank() && endDate.isNotBlank()) {
                database.asteroidDao.getAsteroidsByPeriod(startDate,endDate)
            } else database.asteroidDao.getAsteroids()

            asteroidList.asDomainModel()
            }
        }

    }




