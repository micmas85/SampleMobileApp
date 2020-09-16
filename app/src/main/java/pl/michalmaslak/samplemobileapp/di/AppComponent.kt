package pl.michalmaslak.samplemobileapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import pl.michalmaslak.samplemobileapp.di.auth.AuthComponent
import pl.michalmaslak.samplemobileapp.di.main.MainComponent
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.BaseActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {

    val sessionManager: SessionManager // must add here b/c injecting into abstract class

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory
}