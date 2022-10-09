package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.squareup.moshi.JsonReader
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.Api
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.parsePicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidsDatabase) {

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
                if (startDate.isNotBlank() && endDate.isNotBlank()) {
                    database.asteroidDao.getAsteroidsByPeriod(startDate, endDate)
                } else database.asteroidDao.getAsteroids()

            asteroidList.asDomainModel()
        }
    }

    suspend fun getPictureOfTheDay(): LiveData<PictureOfDay> = liveData(Dispatchers.IO) {
        try {
            val pictureResponse = Api.retrofitService.getPictureOfTheDay()
            val picture = parsePicture(JSONObject(pictureResponse))
            emit(picture!!)
        } catch (e: Exception) {
            Log.e("AsteroidRadar", e.stackTraceToString())
        }
    }

}




