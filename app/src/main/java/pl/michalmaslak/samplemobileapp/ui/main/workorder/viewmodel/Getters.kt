package pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_FILTER_CREATED_AT
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_ORDER_ASC

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let{
        return it.workOrderFields.page?: return 1
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getIsQueryExhausted(): Boolean{
    getCurrentViewStateOrNew().let{
        return it.workOrderFields.isQueryExhausted?: false
    }
}


@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getSearchQuery(): String{
    getCurrentViewStateOrNew().let{
        return it.workOrderFields.searchQuery?: return ""
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getFilter(): String{
    getCurrentViewStateOrNew().let{
        return it.workOrderFields.filter?: WORK_ORDER_FILTER_CREATED_AT
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getOrder(): String{
    getCurrentViewStateOrNew().let{
        return it.workOrderFields.order?: WORK_ORDER_ORDER_ASC
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getWorkOrder(): WorkOrder{
    getCurrentViewStateOrNew().let{
        return it.viewWorkOrderFields.workOrder?.let {
            return it
        }?: getDummyWorkOrder()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getDummyWorkOrder(): WorkOrder {
    return WorkOrder(-1,"","","","",1,1,"")
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.getWorkOrderPk(): Int{
    getCurrentViewStateOrNew().let{
        return it.viewWorkOrderFields.workOrder!!.pk
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun WorkOrderViewModel.isCreatorOfWorkOrder(): Boolean {
    getCurrentViewStateOrNew().let {
        return it.viewWorkOrderFields.isCreatorOfWorkOrder?: false
    }
}