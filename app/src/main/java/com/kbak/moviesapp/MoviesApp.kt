package com.kbak.moviesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // REQUIRED FOR HILT TO WORK!
class MoviesApp : Application()