package pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.models.WorkOrder

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setWorkOrderListData(workOrderList: List<WorkOrder>){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.workOrderList = workOrderList
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setWorkOrder(workOrder: WorkOrder){
    val update = getCurrentViewStateOrNew()
    update.viewWorkOrderFields.workOrder = workOrder
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setWorkOrderStatus(status: String){
    val update = getCurrentViewStateOrNew()
    update.viewWorkOrderFields.workOrder?.status = status
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setIsCreatorOfWorkOrder(isCreatorOfWorkOrder: Boolean){
    val update = getCurrentViewStateOrNew()
    update.viewWorkOrderFields.isCreatorOfWorkOrder = isCreatorOfWorkOrder
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setQueryExhausted(isQueryExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.isQueryExhausted = isQueryExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setQueryInProgress(isQueryInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.workOrderFields.isQueryInProgress = isQueryInProgress
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setWorkOrderFilter(filter: String?){
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.workOrderFields.filter = filter
        setViewState(update)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.setWorkOrderOrder(order: String){
        val update = getCurrentViewStateOrNew()
        update.workOrderFields.order = order
        setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.removeDeletedWorkOrder() {
    val update = getCurrentViewStateOrNew()
    val list = update.workOrderFields.workOrderList?.toMutableList()
    if (list != null) {
        for (i in 0..(list.size - 1)) {
            if (list[i] == getWorkOrder()) {
                list.remove(getWorkOrder())
                break
            }
        }
        setWorkOrderListData(list)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.updateListItem(){
    val update = getCurrentViewStateOrNew()
    val list = update.workOrderFields.workOrderList?.toMutableList()
    if(list != null){
        val newWorkOrder = getWorkOrder()
        for(i in 0..(list.size - 1)){
            if(list[i].pk == newWorkOrder.pk){
                list[i] = newWorkOrder
                break
            }
        }
        update.workOrderFields.workOrderList = list
        setViewState(update)
    }
}
