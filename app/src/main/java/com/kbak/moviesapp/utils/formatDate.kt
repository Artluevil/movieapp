package com.kbak.moviesapp.utils

fun formatDate(date: String?): String {
    return date?.replace("-", "/") ?: "Unknown"
}