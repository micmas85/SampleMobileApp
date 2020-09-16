package pl.michalmaslak.samplemobileapp.repository.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.DataState
import pl.michalmaslak.samplemobileapp.util.StateEvent

@FlowPreview
@MainScope
interface CreateWorkOrderRepository {

    fun createNewWorkOrder(
        authToken: AuthToken,
        title: String,
        description: String,
        stateEvent: StateEvent
    ): Flow<DataState<CreateWorkOrderViewState>>
}