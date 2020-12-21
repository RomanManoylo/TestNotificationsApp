package com.manoilo.testnotificationsapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manoilo.testnotificationsapp.CURRENT_PAGE_NUMBER
import com.manoilo.testnotificationsapp.PAGE_COUNT

class MainViewModel(
    private val sharedPreferences: SharedPreferences,
    private val currentPage: Int = -1
) : ViewModel() {

    val pagesCount by lazy { MutableLiveData<Int>() }
    val currentPageNumber by lazy { MutableLiveData<Int>() }

    init {
        pagesCount.value = sharedPreferences.getInt(PAGE_COUNT, 1)
        if (currentPage == -1)
            currentPageNumber.value = sharedPreferences.getInt(CURRENT_PAGE_NUMBER, 1)
        else
            currentPageNumber.value = currentPage
    }

    fun setCurrentPage(position: Int) {
        currentPageNumber.value = position
        sharedPreferences.edit().putInt(CURRENT_PAGE_NUMBER, position).apply()
    }

    fun addPage() {
        pagesCount.value = pagesCount.value!!.plus(1)
        sharedPreferences.edit().putInt(PAGE_COUNT, pagesCount.value!!)
            .apply()
        setCurrentPage(pagesCount.value!! - 1)
    }

    fun deletePage() {
        pagesCount.value = pagesCount.value!!.minus(1)
        sharedPreferences.edit().putInt(PAGE_COUNT, pagesCount.value!!)
            .apply()
        setCurrentPage(pagesCount.value!! - 1)
    }
}