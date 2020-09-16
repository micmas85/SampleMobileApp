package pl.michalmaslak.samplemobileapp.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.michalmaslak.samplemobileapp.di.main.keys.MainViewModelKey
import pl.michalmaslak.samplemobileapp.ui.main.account.AccountViewModel
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.CreateWorkOrderViewModel
import pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel.WorkOrderViewModel
import pl.michalmaslak.samplemobileapp.viewmodels.MainViewModelFactory

@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(WorkOrderViewModel::class)
    abstract fun bindWorkOrderViewModel(workOrderViewModel: WorkOrderViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @MainViewModelKey(CreateWorkOrderViewModel::class)
    abstract fun bindCreateWorkOrderViewModel(createWorkOrderViewModel: CreateWorkOrderViewModel): ViewModel
}