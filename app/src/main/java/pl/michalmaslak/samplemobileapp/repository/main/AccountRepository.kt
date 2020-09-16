package pl.michalmaslak.samplemobileapp.repository.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountViewState
import pl.michalmaslak.samplemobileapp.util.DataState
import pl.michalmaslak.samplemobileapp.util.StateEvent

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>
}