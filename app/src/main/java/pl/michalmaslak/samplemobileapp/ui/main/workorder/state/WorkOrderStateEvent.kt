package pl.michalmaslak.samplemobileapp.ui.main.workorder.state

import pl.michalmaslak.samplemobileapp.util.StateEvent

sealed class WorkOrderStateEvent: StateEvent{

    class WorkOrderSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ): WorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for work orders."
        }
        override fun toString(): String {
            return "WorkOrderSearchEvent"
        }
    }

    class CheckCreatorOfWorkOrder: WorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "Error checking if you are the creator of this work order."
        }
        override fun toString(): String {
            return "CheckCreatorOfWorkOrder"
        }
    }

    class DeleteWorkOrderEvent: WorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "Error deleting that work order."
        }
        override fun toString(): String {
            return "DeleteWorkOrderEvent"
        }
    }

    data class UpdateWorkOrderEvent(
        val pk: Int,
        val status: String
    ): WorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "Error updating that work order."
        }
        override fun toString(): String {
            return "UpdateWorkOrderEvent"
        }
    }

    class None: WorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}