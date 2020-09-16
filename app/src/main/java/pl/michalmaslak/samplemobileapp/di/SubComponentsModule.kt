package pl.michalmaslak.samplemobileapp.di

import dagger.Module
import pl.michalmaslak.samplemobileapp.di.auth.AuthComponent
import pl.michalmaslak.samplemobileapp.di.main.MainComponent

@Module(
    subcomponents = [
    AuthComponent::class,
    MainComponent::class
    ]
)
class SubComponentsModule {

}