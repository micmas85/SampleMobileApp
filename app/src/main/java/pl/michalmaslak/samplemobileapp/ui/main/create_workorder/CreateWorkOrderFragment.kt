package pl.michalmaslak.samplemobileapp.ui.main.create_workorder

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_create_workorder.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.ui.AreYouSureCallback
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CREATE_WORK_ORDER_VIEW_STATE_KEY
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderStateEvent
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.state.CreateWorkOrderViewState
import pl.michalmaslak.samplemobileapp.util.MessageType
import pl.michalmaslak.samplemobileapp.util.Response
import pl.michalmaslak.samplemobileapp.util.StateMessageCallback
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.SUCCESS_WORK_ORDER_CREATED
import pl.michalmaslak.samplemobileapp.util.UIComponentType
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class CreateWorkOrderFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseCreateWorkOrderFragment(R.layout.fragment_create_workorder, viewModelFactory){


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(CREATE_WORK_ORDER_VIEW_STATE_KEY, viewModel.viewState.value)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //restore state after process death
        savedInstanceState?.let{ inState ->
            (inState[CREATE_WORK_ORDER_VIEW_STATE_KEY] as CreateWorkOrderViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_new_work_order_menu, menu)
    }

    fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.workOrderFields.let{ newWorkOrderFields ->
                setWorkOrderProperties(
                    newWorkOrderFields.newWorkOrderTitle,
                    newWorkOrderFields.newWorkOrderDescription
                )
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (it.response.message.equals(SUCCESS_WORK_ORDER_CREATED)) {
                    viewModel.clearNewWorkOrderFields()
                }
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }


    private fun setWorkOrderProperties(title: String?, description: String?){
        work_order_title.setText(title)
        work_order_description.setText(description)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create_work_order -> {
                val callback: AreYouSureCallback = object: AreYouSureCallback {

                    override fun proceed() {
                        Log.d(TAG, "proceed")
                        createNewWorkOrder()
                    }

                    override fun cancel() {
                        // ignore
                    }

                }
                uiCommunicationListener.onResponseReceived(
                    response = Response(
                        message =getString(R.string.are_you_sure_add),
                        uiComponentType = UIComponentType.AreYouSureDialog(callback),
                        messageType = MessageType.Info()
                    ),
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewWorkOrder() {
        viewModel.setStateEvent(
            CreateWorkOrderStateEvent.CreateNewWorkOrderEvent(
                title = work_order_title.text.toString(),
                description = work_order_description.text.toString()
            )
        )
        uiCommunicationListener.hideSoftKeyboard()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setNewWorkOrderFields(
            work_order_title.text.toString(),
            work_order_description.text.toString()
        )
    }
}