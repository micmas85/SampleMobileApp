package pl.michalmaslak.samplemobileapp.di.main

import dagger.Module
import dagger.Provides
import pl.michalmaslak.samplemobileapp.api.main.SampleApiMainService
import pl.michalmaslak.samplemobileapp.persistence.AccountPropertiesDao
import pl.michalmaslak.samplemobileapp.persistence.AppDatabase
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderDao
import pl.michalmaslak.samplemobileapp.repository.main.AccountRepositoryImpl
import pl.michalmaslak.samplemobileapp.repository.main.CreateWorkOrderRepositoryImpl
import pl.michalmaslak.samplemobileapp.repository.main.WorkOrderRepositoryImpl
import pl.michalmaslak.samplemobileapp.session.SessionManager
import retrofit2.Retrofit

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideSampleApiMainService(retrofitBuilder: Retrofit.Builder): SampleApiMainService{
        return retrofitBuilder
            .build()
            .create(SampleApiMainService::class.java)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        sampleApiMainService: SampleApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepositoryImpl{
        return AccountRepositoryImpl(sampleApiMainService, accountPropertiesDao, sessionManager)
    }
    @JvmStatic
    @MainScope
    @Provides
    fun provideWorkOrderDao(db:AppDatabase): WorkOrderDao{
        return db.getWorkOrderDao()
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideWorkOrderRepository(
        sampleApiMainService: SampleApiMainService,
        workOrderDao: WorkOrderDao,
        sessionManager: SessionManager
    ): WorkOrderRepositoryImpl{
        return WorkOrderRepositoryImpl(sampleApiMainService, workOrderDao, sessionManager)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideCreateWorkOrderRepository(
        sampleApiMainService: SampleApiMainService,
        workOrderDao: WorkOrderDao,
        sessionManager: SessionManager
    ): CreateWorkOrderRepositoryImpl{
        return CreateWorkOrderRepositoryImpl(sampleApiMainService, workOrderDao, sessionManager)
    }
}