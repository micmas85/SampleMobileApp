package pl.michalmaslak.samplemobileapp.repository.main

import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.michalmaslak.samplemobileapp.api.GenericResponse
import pl.michalmaslak.samplemobileapp.api.main.SampleApiMainService
import pl.michalmaslak.samplemobileapp.api.main.responses.WorkOrderCreateUpdateResponse
import pl.michalmaslak.samplemobileapp.api.main.responses.WorkOrderListSearchResponse
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderDao
import pl.michalmaslak.samplemobileapp.persistence.returnOrderedWorkOrderQuery
import pl.michalmaslak.samplemobileapp.repository.NetworkBoundResource
import pl.michalmaslak.samplemobileapp.repository.buildError
import pl.michalmaslak.samplemobileapp.repository.safeApiCall
import pl.michalmaslak.samplemobileapp.session.SessionManager
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.RESPONSE_NO_PERMISSION_TO_EDIT
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.SUCCESS_WORK_ORDER_DELETED
import javax.inject.Inject


@FlowPreview
@MainScope
class WorkOrderRepositoryImpl
@Inject
constructor(
    val sampleApiMainService: SampleApiMainService,
    val workOrderDao: WorkOrderDao,
    val sessionManager: SessionManager
): WorkOrderRepository
{

    private val TAG: String = "AppDebug"
    override fun searchWorkOrders(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<WorkOrderViewState>> {
        return object: NetworkBoundResource<WorkOrderListSearchResponse, List<WorkOrder>, WorkOrderViewState>(
            dispatcher = IO,
            stateEvent = stateEvent,
            apiCall = {
                sampleApiMainService.searchListWorkOrders(
                    "Token ${authToken.token!!}",
                    query = query,
                    ordering = filterAndOrder,
                    page = page
                )
            },
            cacheCall = {
                workOrderDao.returnOrderedWorkOrderQuery(
                    query = query,
                    filterAndOrder = filterAndOrder,
                    page = page
                )
            }
        ){
            override suspend fun updateCache(networkObject: WorkOrderListSearchResponse) {
                val workOrderPostList = networkObject.toList()
                withContext(IO) {
                    for(workOrderPost in workOrderPostList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting workOrder: ${workOrderPost}")
                                workOrderDao.insert(workOrderPost)
                            }
                        }catch (e: Exception){
                            Log.e(TAG, "updateLocalDb: error updating cache data on workOrder post with pk: ${workOrderPost.pk}. " +
                                    "${e.message}")
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many workOrders being inserted/updated.
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<WorkOrder>): DataState<WorkOrderViewState> {
                val viewState = WorkOrderViewState(
                    workOrderFields = WorkOrderViewState.WorkOrderFields(
                        workOrderList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun isCreatorOfWorkOrder(
        authToken: AuthToken,
        pk: Int,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO){
            sampleApiMainService.isCreatorOfWorkOrder(
                "Token ${authToken.token!!}",
                pk
            )
        }
        emit(
            object: ApiResponseHandler<WorkOrderViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<WorkOrderViewState> {
                    val viewState = WorkOrderViewState(
                        viewWorkOrderFields = WorkOrderViewState.ViewWorkOrderFields(
                            isCreatorOfWorkOrder = false
                        )
                    )
                    return when {

                        resultObj.response.equals(RESPONSE_NO_PERMISSION_TO_EDIT) -> {
                            DataState.data(
                                response = null,
                                data = viewState,
                                stateEvent = stateEvent
                            )
                        }

                        resultObj.response.equals(RESPONSE_HAS_PERMISSION_TO_EDIT) -> {
                            viewState.viewWorkOrderFields.isCreatorOfWorkOrder = true
                            DataState.data(
                                response = null,
                                data = viewState,
                                stateEvent = stateEvent
                            )
                        }

                        else -> {
                            buildError(
                                ERROR_UNKNOWN,
                                UIComponentType.None(),
                                stateEvent
                            )
                        }
                    }
                }
            }.getResult()
        )
    }

    override fun deleteWorkOrder(
        authToken: AuthToken,
        workOrder: WorkOrder,
        stateEvent: StateEvent
    ) =  flow {
        val apiResult = safeApiCall(IO){
            sampleApiMainService.deleteWorkOrder(
                "Token ${authToken.token!!}",
                workOrder.pk
            )
        }
        emit(
            object: ApiResponseHandler<WorkOrderViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<WorkOrderViewState> {

                    if(resultObj.response == SUCCESS_WORK_ORDER_DELETED){
                        workOrderDao.deleteWorkOrder(workOrder)
                        return DataState.data(
                            response = Response(
                                message = SUCCESS_WORK_ORDER_DELETED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            stateEvent = stateEvent
                        )
                    }
                    else{
                        return buildError(
                            ERROR_UNKNOWN,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }

    override fun updateWorkOrder(
        authToken: AuthToken,
        pk: Int,
        status: String,
        stateEvent: StateEvent
    ) = flow{

        val apiResult = safeApiCall(IO){
            sampleApiMainService.updateWorkOrder(
                "Token ${authToken.token!!}",
                pk,
                status
            )
        }
        emit(
            object: ApiResponseHandler<WorkOrderViewState, WorkOrderCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: WorkOrderCreateUpdateResponse): DataState<WorkOrderViewState> {

                    val updatedWorkOrder = resultObj.toWorkOrder()

                    workOrderDao.updateWorkOrder(
                        updatedWorkOrder.pk,
                        updatedWorkOrder.status
                    )

                    return DataState.data(
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data =  WorkOrderViewState(
                            viewWorkOrderFields = WorkOrderViewState.ViewWorkOrderFields(
                                workOrder = updatedWorkOrder
                            )
                        ),
                        stateEvent = stateEvent
                    )

                }

            }.getResult()
        )
    }



}








