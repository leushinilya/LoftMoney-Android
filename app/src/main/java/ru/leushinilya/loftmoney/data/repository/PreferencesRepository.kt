package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Completable
import io.reactivex.Maybe

interface PreferencesRepository {

    fun getAuthToken(): Maybe<String>
    fun setAuthToken(value: String): Completable

    fun clearAll(): Completable

}