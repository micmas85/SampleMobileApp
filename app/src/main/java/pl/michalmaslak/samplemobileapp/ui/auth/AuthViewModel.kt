package pl.michalmaslak.samplemobileapp.ui.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.repository.auth.AuthRepository
import pl.michalmaslak.samplemobileapp.ui.BaseViewModel
import pl.michalmaslak.samplemobileapp.ui.auth.state.AuthStateEvent.*
import pl.michalmaslak.samplemobileapp.ui.auth.state.AuthViewState
import pl.michalmaslak.samplemobileapp.ui.auth.state.LoginFields
import pl.michalmaslak.samplemobileapp.ui.auth.state.RegistrationFields
import pl.michalmaslak.samplemobileapp.ui.auth.state.ResetPasswordFields
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AuthScope
class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthViewState>()
{

    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
        data.resetPasswordResponse?.let{
            setResetPasswordResponse(it)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<AuthViewState>> = when(stateEvent){

            is LoginAttemptEvent -> {
                authRepository.attemptLogin(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                authRepository.attemptRegistration(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is ResetPasswordAttemptEvent -> {
                authRepository.attemptResetPassword(
                    stateEvent = stateEvent,
                    email = stateEvent.email
                )
            }

            is CheckPreviousAuthEvent -> {
                authRepository.checkPreviousAuthUser(stateEvent)
            }

            else -> {
                flow{
                    emit(
                        value = DataState.error(
                            response = Response(
                                message = INVALID_STATE_EVENT,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setResetPasswordFields(resetPasswordFields: ResetPasswordFields){
        val update = getCurrentViewStateOrNew()
        if(update.resetPasswordFields == resetPasswordFields){
            return
        }
        update.resetPasswordFields = resetPasswordFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    fun setResetPasswordResponse(resetPasswordResponse: String?){
        val update = getCurrentViewStateOrNew()
        if(update.resetPasswordResponse == resetPasswordResponse){
            return
        }
        update.resetPasswordResponse = resetPasswordResponse
        setViewState(update)
    }

}