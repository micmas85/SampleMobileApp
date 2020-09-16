package pl.michalmaslak.samplemobileapp.di.auth

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import pl.michalmaslak.samplemobileapp.api.auth.SampleApiAuthService
import pl.michalmaslak.samplemobileapp.persistence.AccountPropertiesDao
import pl.michalmaslak.samplemobileapp.persistence.AuthTokenDao
import pl.michalmaslak.samplemobileapp.repository.auth.AuthRepository
import pl.michalmaslak.samplemobileapp.repository.auth.AuthRepositoryImpl
import pl.michalmaslak.samplemobileapp.session.SessionManager
import retrofit2.Retrofit

@Module
object AuthModule{

    @JvmStatic
    @AuthScope
    @Provides
    fun provideSampleApiAuthService(retrofitBuilder: Retrofit.Builder): SampleApiAuthService{
        return retrofitBuilder
            .build()
            .create(SampleApiAuthService::class.java)
    }

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        sampleApiAuthService: SampleApiAuthService,
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            sampleApiAuthService,
            sessionManager,
            sharedPreferences,
            editor
        )
    }

}