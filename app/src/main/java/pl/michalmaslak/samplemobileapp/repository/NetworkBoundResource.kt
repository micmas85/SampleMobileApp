package pl.michalmaslak.samplemobileapp.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.NETWORK_ERROR
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.UNKNOWN_ERROR

@FlowPreview
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkObj?,
    private val cacheCall: suspend () -> CacheObj?
)
{

    private val TAG: String = "AppDebug"

    val result: Flow<DataState<ViewState>> = flow {

        emit(returnCache(markJobComplete = false))

        val apiResult = safeApiCall(dispatcher){apiCall.invoke()}

        when(apiResult){
            is ApiResult.GenericError -> {
                emit(
                    buildError(
                        apiResult.errorMessage?.let { it }?: UNKNOWN_ERROR,
                        UIComponentType.Dialog(),
                        stateEvent
                    )
                )
            }

            is ApiResult.NetworkError -> {
                emit(
                    buildError(
                        NETWORK_ERROR,
                        UIComponentType.Dialog(),
                        stateEvent
                    )
                )
            }

            is ApiResult.Success -> {
                if(apiResult.value == null){
                    emit(
                        buildError(
                            UNKNOWN_ERROR,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    )
                }
                else{
                    updateCache(apiResult.value as NetworkObj)
                }
            }
        }

        emit(returnCache(markJobComplete = true))
    }

    private suspend fun returnCache(markJobComplete: Boolean): DataState<ViewState> {

        val cacheResult = safeCacheCall(dispatcher){cacheCall.invoke()}

        var jobCompleteMarker: StateEvent? = null
        if(markJobComplete){
            jobCompleteMarker = stateEvent
        }

        return object: CacheResponseHandler<ViewState, CacheObj>(
            response = cacheResult,
            stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState> {
                return handleCacheSuccess(resultObj)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState> // make sure to return null for stateEvent


}






