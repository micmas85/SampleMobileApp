package pl.michalmaslak.samplemobileapp.di.auth

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import pl.michalmaslak.samplemobileapp.fragments.auth.AuthFragmentFactory

@Module
object AuthFragmentsModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AuthFragmentFactory(
            viewModelFactory
        )
    }

}