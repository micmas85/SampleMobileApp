package pl.michalmaslak.samplemobileapp.repository.main

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.api.main.SampleApiMainService
import pl.michalmaslak.samplemobileapp.api.main.responses.WorkOrderCreateUpdateResponse
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderDao
import pl.michalmaslak.samplemobileapp.repository.safeApiCall
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.*
import javax.inject.Inject


@FlowPreview
@MainScope
class CreateWorkOrderRepositoryImpl
@Inject
constructor(
    val sampleApiMainService: SampleApiMainService,
    val workOrderDao: WorkOrderDao,
    val sessionManager: SessionManager
): CreateWorkOrderRepository {

    private val TAG: String = "AppDebug"

    override fun createNewWorkOrder(
        authToken: AuthToken,
        title: String,
        description: String,
        stateEvent: StateEvent
    ) = flow{

        val apiResult = safeApiCall(IO){

            sampleApiMainService.createWorkOrder(
                "Token ${authToken.token!!}",
                title,
                description
            )
        }

        emit(
            object: ApiResponseHandler<CreateWorkOrderViewState, WorkOrderCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: WorkOrderCreateUpdateResponse): DataState<CreateWorkOrderViewState> {

//                    val updatedWorkOrder = resultObj.toWorkOrder()
//                    workOrderDao.insert(updatedWorkOrder)

                    return DataState.data(
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

}
