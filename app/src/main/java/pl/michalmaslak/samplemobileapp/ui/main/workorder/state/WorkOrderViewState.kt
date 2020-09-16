package pl.michalmaslak.samplemobileapp.ui.main.workorder.state

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.michalmaslak.samplemobileapp.models.WorkOrder

const val WORK_ORDER_VIEW_STATE_KEY = "pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState"

@Parcelize
data class WorkOrderViewState(
    var workOrderFields: WorkOrderFields = WorkOrderFields(),
    var viewWorkOrderFields: ViewWorkOrderFields = ViewWorkOrderFields()
) : Parcelable {
    @Parcelize
    data class WorkOrderFields(
        var workOrderList: List<WorkOrder>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryInProgress: Boolean? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewWorkOrderFields(
        var workOrder: WorkOrder? = null,
        var isCreatorOfWorkOrder: Boolean? = null
    ) : Parcelable

}