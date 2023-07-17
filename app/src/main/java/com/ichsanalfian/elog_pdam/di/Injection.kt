package com.ichsanalfian.elog_pdam.di

import com.ichsanalfian.elog_pdam.api.ApiConfig

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository(apiService)
    }
}