package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from Databaseasteroids ORDER BY closeApproachDate")
    fun getAsteroids(): List<DatabaseAsteroids>

    @Query("select * from Databaseasteroids WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate")
    fun getAsteroidsByPeriod(startDate: String, endDate: String): List<DatabaseAsteroids>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (asteroids: List<DatabaseAsteroids>)
}

@Database(entities = [DatabaseAsteroids::class], version = 1, exportSchema = false)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}