package pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state

import pl.michalmaslak.samplemobileapp.util.StateEvent

sealed class CreateWorkOrderStateEvent: StateEvent {

    data class CreateNewWorkOrderEvent(
        val title: String,
        val description: String
    ): CreateWorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "Unable to create new work order."
        }
    }

    class None: CreateWorkOrderStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}