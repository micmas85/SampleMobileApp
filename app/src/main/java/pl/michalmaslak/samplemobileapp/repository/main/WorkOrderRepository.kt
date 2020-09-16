package pl.michalmaslak.samplemobileapp.repository.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.DataState
import pl.michalmaslak.samplemobileapp.util.StateEvent

@FlowPreview
@MainScope
interface WorkOrderRepository {

    fun searchWorkOrders(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<WorkOrderViewState>>

    fun isCreatorOfWorkOrder(
        authToken: AuthToken,
        pk: Int,
        stateEvent: StateEvent
    ): Flow<DataState<WorkOrderViewState>>

    fun deleteWorkOrder(
        authToken: AuthToken,
        workOrder: WorkOrder,
        stateEvent: StateEvent
    ): Flow<DataState<WorkOrderViewState>>

    fun updateWorkOrder(
        authToken: AuthToken,
        pk: Int,
        status: String,
        stateEvent: StateEvent
    ): Flow<DataState<WorkOrderViewState>>

}