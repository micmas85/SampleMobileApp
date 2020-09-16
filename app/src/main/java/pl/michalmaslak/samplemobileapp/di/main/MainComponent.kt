package pl.michalmaslak.samplemobileapp.di.main

import dagger.Subcomponent
import pl.michalmaslak.samplemobileapp.ui.main.MainActivity

@MainScope
@Subcomponent(
    modules = [
        MainModule::class,
        MainViewModelModule::class,
        MainFragmentsModule::class
    ]
)
interface MainComponent {

    @Subcomponent.Factory
    interface Factory{

        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
}