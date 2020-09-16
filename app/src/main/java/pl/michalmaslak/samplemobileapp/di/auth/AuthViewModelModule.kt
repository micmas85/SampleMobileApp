package pl.michalmaslak.samplemobileapp.di.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.michalmaslak.samplemobileapp.di.auth.keys.AuthViewModelKey
import pl.michalmaslak.samplemobileapp.ui.auth.AuthViewModel
import pl.michalmaslak.samplemobileapp.viewmodels.AuthViewModelFactory

@Module
abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    abstract fun bindViewModelFactory(factory: AuthViewModelFactory): ViewModelProvider.Factory

    @AuthScope
    @Binds
    @IntoMap
    @AuthViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}