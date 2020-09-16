package pl.michalmaslak.samplemobileapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import pl.michalmaslak.samplemobileapp.ui.auth.state.AuthStateEvent
import pl.michalmaslak.samplemobileapp.ui.auth.state.ResetPasswordFields
import pl.michalmaslak.samplemobileapp.util.ErrorHandling
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class ForgotPasswordFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseAuthFragment(R.layout.fragment_forgot_password, viewModelFactory) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        reset_password_button.setOnClickListener{
            resetPassword()
        }

        return_to_launcher_fragment.setOnClickListener {
            viewModel.setResetPasswordResponse(null)
            findNavController().popBackStack()
        }
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.resetPasswordFields?.let { resetPasswordFields ->
                resetPasswordFields.reset_password_email?.let{ input_email.setText(it) }
            }
            it.resetPasswordResponse?.let { resetPasswordResponse ->
                resetPasswordResponse?.let{ response ->
                    Log.d(TAG,"resetPasswordResponse: response: $response")
                    if(!response.equals(ErrorHandling.GENERIC_AUTH_ERROR)){
                        onPasswordResetLinkSent()
                    }
                }
            }

        })


    }

    fun resetPassword(){
        viewModel.setStateEvent(
            AuthStateEvent.ResetPasswordAttemptEvent(
                input_email.text.toString()
            )
        )
    }

    fun onPasswordResetLinkSent() {
        CoroutineScope(Main).launch {
            Log.d(TAG, "onPasswordResetLinkSent: email sent")
            forgot_password_layout.visibility = View.INVISIBLE

            val animation = TranslateAnimation(
                password_reset_done_container.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setResetPasswordFields(
            ResetPasswordFields(
                input_email.text.toString()
            )
        )
    }

}