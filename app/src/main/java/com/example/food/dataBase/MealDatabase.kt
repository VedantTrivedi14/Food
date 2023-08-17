package com.example.food.dataBase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.food.model.Meal

@Database(entities = [Meal::class], version = 1,exportSchema = false)
@TypeConverters(MealTypeConverter::class)
abstract  class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        var INSTANCE: MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
                Log.i("#database","Database created successfully")
            }
            return INSTANCE as MealDatabase
        }
    }
}