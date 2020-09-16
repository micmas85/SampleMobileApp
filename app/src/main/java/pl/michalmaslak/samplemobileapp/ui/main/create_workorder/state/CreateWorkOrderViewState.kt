package pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CREATE_WORK_ORDER_VIEW_STATE_KEY = "pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState"

@Parcelize
data class CreateWorkOrderViewState(
    var workOrderFields: NewWorkOrderFields = NewWorkOrderFields()
) : Parcelable {
    @Parcelize
    data class NewWorkOrderFields(
        var newWorkOrderTitle: String? = null,
        var newWorkOrderDescription: String? = null
        ) : Parcelable
}