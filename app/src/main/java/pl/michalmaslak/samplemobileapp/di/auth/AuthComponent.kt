package pl.michalmaslak.samplemobileapp.di.auth

import dagger.Subcomponent
import pl.michalmaslak.samplemobileapp.ui.auth.AuthActivity

@AuthScope
@Subcomponent(
    modules = [
    AuthModule::class,
    AuthViewModelModule::class,
    AuthFragmentsModule::class
    ]
)
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory{

        fun create(): AuthComponent
    }

    fun inject(authActivity: AuthActivity)
}