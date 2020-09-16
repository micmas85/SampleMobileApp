package pl.michalmaslak.samplemobileapp.di.main

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import pl.michalmaslak.samplemobileapp.fragments.main.account.AccountFragmentFactory
import pl.michalmaslak.samplemobileapp.fragments.main.create_work_order.CreateWorkOrderFragmentFactory
import pl.michalmaslak.samplemobileapp.fragments.main.work_order.WorkOrderFragmentFactory
import javax.inject.Named

@Module
object MainFragmentsModule {

    @JvmStatic
    @MainScope
    @Provides
    @Named("AccountFragmentFactory")
    fun provideAccountFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AccountFragmentFactory(
            viewModelFactory
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("WorkOrderFragmentFactory")
    fun provideWorkOrderFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return WorkOrderFragmentFactory(
            viewModelFactory
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("CreateWorkOrderFragmentFactory")
    fun provideCreateWorkOrderFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return CreateWorkOrderFragmentFactory(
            viewModelFactory
        )
    }
}