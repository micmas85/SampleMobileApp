package pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_FILTER_CREATED_AT
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_ORDER_ASC
import pl.michalmaslak.samplemobileapp.repository.main.WorkOrderRepositoryImpl
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.BaseViewModel
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent.*
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import pl.michalmaslak.samplemobileapp.util.PreferenceKeys.Companion.WORK_ORDER_FILTER
import pl.michalmaslak.samplemobileapp.util.PreferenceKeys.Companion.WORK_ORDER_ORDER
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@MainScope
class WorkOrderViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val workOrderRepository: WorkOrderRepositoryImpl,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<WorkOrderViewState>(){

    init{
        setWorkOrderFilter(sharedPreferences.getString(WORK_ORDER_FILTER, WORK_ORDER_FILTER_CREATED_AT))


            
        setWorkOrderOrder(sharedPreferences.getString(WORK_ORDER_ORDER, WORK_ORDER_ORDER_ASC))

    }

    override fun handleNewData(data: WorkOrderViewState) {

        data.workOrderFields.let { workOrderFields ->

            workOrderFields.workOrderList?.let { workOrderList ->
                handleIncomingWorkOrderListData(data)
            }

            workOrderFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhausted(isQueryExhausted)
            }

        }

        data.viewWorkOrderFields.let { viewWorkOrderFields ->

            viewWorkOrderFields.workOrder?.let { workOrder ->
                setWorkOrder(workOrder)
            }

            viewWorkOrderFields.isCreatorOfWorkOrder?.let { isCreator ->
                setIsCreatorOfWorkOrder(isCreator)
            }
        }

    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if(!isJobAlreadyActive(stateEvent)){
            sessionManager.cachedToken.value?.let { authToken ->
                val job: Flow<DataState<WorkOrderViewState>> = when(stateEvent){

                    is WorkOrderSearchEvent -> {
                        if(stateEvent.clearLayoutManagerState){
                            clearLayoutManagerState()
                        }
                        workOrderRepository.searchWorkOrders(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            query = getSearchQuery(),
                            filterAndOrder = getOrder() + getFilter(),
                            page = getPage()
                        )
                    }

                    is CheckCreatorOfWorkOrder -> {
                        workOrderRepository.isCreatorOfWorkOrder(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            pk = getWorkOrderPk()
                        )
                    }

                    is DeleteWorkOrderEvent -> {
                        workOrderRepository.deleteWorkOrder(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            workOrder = getWorkOrder()
                        )
                    }

                    is UpdateWorkOrderEvent -> {
                       workOrderRepository.updateWorkOrder(
                            stateEvent = stateEvent,
                            pk = stateEvent.pk,
                            authToken = authToken,
                            status = stateEvent.status
                        )
                    }

                    else -> {
                        flow{
                            emit(
                                DataState.error(
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
        }
    }



    override fun initNewViewState(): WorkOrderViewState {
        return WorkOrderViewState()
    }

    fun saveFilterOptions(filter: String, order:String){
        editor.putString(WORK_ORDER_FILTER, filter).apply()
        editor.putString(WORK_ORDER_ORDER, order).apply()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}