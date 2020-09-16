package pl.michalmaslak.samplemobileapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_launcher.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LauncherFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseAuthFragment(R.layout.fragment_launcher, viewModelFactory) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            navRegistration()
        }

        login.setOnClickListener {
            navLogin()
        }

        forgot_password.setOnClickListener {
            navForgotPassword()
        }

        focusable_view.requestFocus()
    }

    private fun navForgotPassword() {
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

}