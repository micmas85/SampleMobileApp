package pl.michalmaslak.samplemobileapp.ui

import pl.michalmaslak.samplemobileapp.util.Response
import pl.michalmaslak.samplemobileapp.util.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)

    fun expandAppBar()

    fun hideSoftKeyboard()

}