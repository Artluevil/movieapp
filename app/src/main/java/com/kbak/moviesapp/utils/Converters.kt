package com.kbak.moviesapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromGenreIdsList(value: List<Int>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toGenreIdsList(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
