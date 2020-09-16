package pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent.WorkOrderSearchEvent
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.page = 1
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.refreshFromCache() {
    if (!isJobAlreadyActive(WorkOrderSearchEvent())) {
        setQueryExhausted(false)
        setStateEvent(WorkOrderSearchEvent(false))
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.loadFirstPage() {
    if (!isJobAlreadyActive(WorkOrderSearchEvent())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(WorkOrderSearchEvent())
        Log.e(TAG,"WorkOrderViewModel: loadFirstPage: ${viewState.value!!.workOrderFields.searchQuery}")
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().workOrderFields.page ?: 1
    update.workOrderFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.nextPage(){
    if(!isJobAlreadyActive(WorkOrderSearchEvent())
        &&!viewState.value!!.workOrderFields.isQueryExhausted!!)
    {
        Log.d(TAG, "WorkOrderViewModel: Attempting to load next page....")
        incrementPageNumber()
        setStateEvent(WorkOrderSearchEvent())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.handleIncomingWorkOrderListData(viewState: WorkOrderViewState){
    viewState.workOrderFields.let{ workOrderFields ->
        workOrderFields.workOrderList?.let{ setWorkOrderListData(it)}
    }
}