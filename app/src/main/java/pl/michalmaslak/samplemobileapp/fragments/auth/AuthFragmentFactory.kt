package pl.michalmaslak.samplemobileapp.fragments.auth

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import pl.michalmaslak.samplemobileapp.ui.auth.ForgotPasswordFragment
import pl.michalmaslak.samplemobileapp.ui.auth.LauncherFragment
import pl.michalmaslak.samplemobileapp.ui.auth.LoginFragment
import pl.michalmaslak.samplemobileapp.ui.auth.RegisterFragment
import javax.inject.Inject


@FlowPreview
@AuthScope
class AuthFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    @ExperimentalCoroutinesApi
    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            LauncherFragment::class.java.name -> {
                LauncherFragment(viewModelFactory)
            }

            LoginFragment::class.java.name -> {
                LoginFragment(viewModelFactory)
            }

            RegisterFragment::class.java.name -> {
                RegisterFragment(viewModelFactory)
            }

            ForgotPasswordFragment::class.java.name -> {
                ForgotPasswordFragment(viewModelFactory)
            }

            else -> {
                LauncherFragment(viewModelFactory)
            }
        }


}