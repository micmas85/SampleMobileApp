package pl.michalmaslak.samplemobileapp

import android.app.Application
import pl.michalmaslak.samplemobileapp.di.AppComponent
import pl.michalmaslak.samplemobileapp.di.DaggerAppComponent
import pl.michalmaslak.samplemobileapp.di.auth.AuthComponent
import pl.michalmaslak.samplemobileapp.di.main.MainComponent

class BaseApplication: Application() {

    lateinit var appComponent: AppComponent

    private var mainComponent: MainComponent? = null

    private var authComponent: AuthComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    fun authComponent(): AuthComponent{
        if(authComponent == null){
            authComponent = appComponent.authComponent().create()
        }
        return authComponent as AuthComponent
    }

    fun mainComponent(): MainComponent {
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    fun releaseAuthComponent(){
        authComponent = null
    }

    fun releaseMainComponent(){
        mainComponent = null
    }

    private fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}