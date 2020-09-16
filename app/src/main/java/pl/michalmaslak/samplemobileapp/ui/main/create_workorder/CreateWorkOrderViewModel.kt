package pl.michalmaslak.samplemobileapp.ui.main.create_workorder

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.repository.main.CreateWorkOrderRepositoryImpl
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.BaseViewModel
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderStateEvent.CreateNewWorkOrderEvent
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState.NewWorkOrderFields
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
@MainScope
class CreateWorkOrderViewModel
@Inject
constructor(
    private val createWorkOrderRepositoryImpl: CreateWorkOrderRepositoryImpl,
    val sessionManager: SessionManager
): BaseViewModel<CreateWorkOrderViewState>(){


    override fun handleNewData(data: CreateWorkOrderViewState) {

        setNewWorkOrderFields(
            data.workOrderFields.newWorkOrderTitle,
            data.workOrderFields.newWorkOrderDescription
        )
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<CreateWorkOrderViewState>> = when (stateEvent) {


                is CreateNewWorkOrderEvent -> {
                    createWorkOrderRepositoryImpl.createNewWorkOrder(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        title = stateEvent.title,
                        description = stateEvent.description
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

    override fun initNewViewState(): CreateWorkOrderViewState {
        return CreateWorkOrderViewState()
    }

    fun setNewWorkOrderFields(title: String?, description:String?){
        val update = getCurrentViewStateOrNew()
        val newWorkOrderFields = update.workOrderFields
        title?.let { newWorkOrderFields.newWorkOrderTitle = it }
        description?.let { newWorkOrderFields.newWorkOrderDescription = it }
        update.workOrderFields = newWorkOrderFields
        setViewState(update)
    }

    fun clearNewWorkOrderFields(){
        val update = getCurrentViewStateOrNew()
        update.workOrderFields = NewWorkOrderFields()
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}