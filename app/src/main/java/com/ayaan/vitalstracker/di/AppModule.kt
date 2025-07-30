package com.ayaan.vitalstracker.di

import androidx.room.Room
import com.ayaan.vitalstracker.data.db.AppDatabase
import com.ayaan.vitalstracker.data.repository.VitalsRepository
import com.ayaan.vitalstracker.ui.presentation.viewmodel.VitalsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    // DAO
    single { get<AppDatabase>().vitalsDao() }

    // Repository
    single { VitalsRepository(get()) }

    // ViewModel
    viewModel { VitalsViewModel(get()) }
}
