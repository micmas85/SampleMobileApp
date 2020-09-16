package pl.michalmaslak.samplemobileapp.repository.auth

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import pl.michalmaslak.samplemobileapp.di.auth.AuthScope
import pl.michalmaslak.samplemobileapp.ui.auth.state.AuthViewState
import pl.michalmaslak.samplemobileapp.util.DataState
import pl.michalmaslak.samplemobileapp.util.StateEvent

@FlowPreview
@AuthScope
interface AuthRepository {

    fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>

    fun attemptResetPassword(
        stateEvent: StateEvent,
        email: String
    ):Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun saveAuthenticatedUserToPrefs(email: String)

    fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState>



}