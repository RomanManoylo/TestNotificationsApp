package com.manoilo.testnotificationsapp.factory

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manoilo.testnotificationsapp.viewmodel.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val sharedPreferences: SharedPreferences, private val currentPage: Int = -1) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainViewModel(sharedPreferences, currentPage) as T
}