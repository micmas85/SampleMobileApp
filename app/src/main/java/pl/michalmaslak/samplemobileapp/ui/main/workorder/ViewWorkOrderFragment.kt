package pl.michalmaslak.samplemobileapp.ui.main.workorder

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_view_workorder.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.ui.AreYouSureCallback
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WORK_ORDER_VIEW_STATE_KEY
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent.CheckCreatorOfWorkOrder
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderStateEvent.DeleteWorkOrderEvent
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState
import pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel.getWorkOrderPk
import pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel.removeDeletedWorkOrder
import pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel.setIsCreatorOfWorkOrder
import pl.michalmaslak.samplemobileapp.util.*
import pl.michalmaslak.samplemobileapp.util.SuccessHandling.Companion.SUCCESS_WORK_ORDER_DELETED
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ViewWorkOrderFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseWorkOrderFragment(R.layout.fragment_view_workorder, viewModelFactory)
{

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value
        viewState?.workOrderFields?.workOrderList = ArrayList()
        outState.putParcelable(WORK_ORDER_VIEW_STATE_KEY, viewState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //restore state after process death
        savedInstanceState?.let{ inState ->
            (inState[WORK_ORDER_VIEW_STATE_KEY] as WorkOrderViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIsCreatorOfWorkOrder()
        subscribeObservers()
        uiCommunicationListener.expandAppBar()

        delete_button.setOnClickListener {
            confirmDeleteRequest()
        }

        start_button.setOnClickListener {
            changeStatus("IN_PROGRESS")
        }

        pause_button.setOnClickListener {
            changeStatus("ON_HOLD")
        }

        stop_button.setOnClickListener {
            changeStatus("COMPLETED")
        }

    }

    private fun changeStatus(status: String){
        viewModel.setStateEvent(
            WorkOrderStateEvent.UpdateWorkOrderEvent(
                viewModel.getWorkOrderPk(),
                status
            )
        )
    }

    private fun confirmDeleteRequest(){
        val callback: AreYouSureCallback = object : AreYouSureCallback {

            override fun proceed() {
                deleteWorkOrder()
            }

            override fun cancel() {
                //ignore
            }
        }
        uiCommunicationListener.onResponseReceived(
            response = Response(
                message = getString(R.string.are_you_sure_delete),
                uiComponentType = UIComponentType.AreYouSureDialog(callback),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    private fun deleteWorkOrder(){
        viewModel.setStateEvent(
            DeleteWorkOrderEvent()
        )

    }

    private fun checkIsCreatorOfWorkOrder(){
        viewModel.setIsCreatorOfWorkOrder(false)
        viewModel.setStateEvent(CheckCreatorOfWorkOrder())
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            viewState.viewWorkOrderFields.workOrder?.let { workOrder ->
                setWorkOrderProperties(workOrder)
            }
            if(viewState.viewWorkOrderFields.isCreatorOfWorkOrder == true){
                adaptViewToCreatorMode()
            }
        } )

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            if(stateMessage?.response?.message.equals(SUCCESS_WORK_ORDER_DELETED)){
                viewModel.removeDeletedWorkOrder()
                findNavController().popBackStack()
            }

            stateMessage?.let {
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

    private fun adaptViewToCreatorMode() {
        delete_button.visibility = View.VISIBLE
    }

    private fun setWorkOrderProperties(workOrder: WorkOrder){
        work_order_title.setText(workOrder.title)
        work_order_description.setText(workOrder.description)
        work_order_created_by.setText(workOrder.created_by)
        work_order_status.setText(workOrder.status)
        work_order_created_at.setText(DateUtils.convertLongToStringDate(workOrder.created_at))
    }
}