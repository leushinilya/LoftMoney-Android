package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Completable
import io.reactivex.Maybe
import ru.leushinilya.loftmoney.data.local.PreferencesDataSource
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : PreferencesRepository {

    override fun getAuthToken(): Maybe<String> = Maybe.fromCallable {
        preferencesDataSource.getString("authToken")
    }

    override fun setAuthToken(value: String): Completable = Completable.fromRunnable {
        preferencesDataSource.saveString("authToken", value)
    }

    override fun clearAll(): Completable = Completable.fromRunnable {
        preferencesDataSource.clearAll()
    }

}