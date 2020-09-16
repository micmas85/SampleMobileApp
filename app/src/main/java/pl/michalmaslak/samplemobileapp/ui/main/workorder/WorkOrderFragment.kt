package pl.michalmaslak.samplemobileapp.ui.main.workorder

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import kotlinx.android.synthetic.main.fragment_workorder.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_FILTER_CREATED_AT
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_FILTER_CREATED_BY
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_ORDER_ASC
import pl.michalmaslak.samplemobileapp.persistence.WorkOrderQueryUtils.Companion.WORK_ORDER_ORDER_DESC
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WORK_ORDER_VIEW_STATE_KEY
import pl.michalmaslak.samplemobileapp.ui.main.workorder.state.WorkOrderViewState
import pl.michalmaslak.samplemobileapp.ui.main.workorder.viewmodel.*
import pl.michalmaslak.samplemobileapp.util.ErrorHandling.Companion.isPaginationDone
import pl.michalmaslak.samplemobileapp.util.StateMessageCallback
import pl.michalmaslak.samplemobileapp.util.TopSpacingItemDecoration
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class WorkOrderFragment
@Inject
constructor(
    val viewModelFactory: ViewModelProvider.Factory
) : BaseWorkOrderFragment(R.layout.fragment_workorder, viewModelFactory),
WorkOrderListAdapter.Interaction,
SwipeRefreshLayout.OnRefreshListener
{


    private lateinit var recyclerAdapter: WorkOrderListAdapter
    private lateinit var searchView: SearchView

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
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCache()
    }

    override fun onPause() {
        super.onPause()
        setLayoutManagerState()
    }
    private fun onWorkOrderSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private fun resetUI(){
        work_order_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun setLayoutManagerState(){
        work_order_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            Log.d(TAG,"WorkOrderFragment, viewState: $viewState")
            if(viewState != null){
                recyclerAdapter.submitList(
                    workOrderList = viewState.workOrderFields.workOrderList,
                    isQueryExhausted = viewState.workOrderFields.isQueryExhausted?: true
                )
            }
        } )

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if(isPaginationDone(stateMessage.response.message)){
                    viewModel.setQueryExhausted(true)
                    viewModel.clearStateMessage()
                }else{
                    uiCommunicationListener.onResponseReceived(
                        response = it.response,
                        stateMessageCallback = object: StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })
    }

    private fun initSearchView(menu: Menu){
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }
        //enter on pc keyboard or virtual
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH){
                val searchQuery = v.text.toString()
                Log.e(TAG,"SearchView: (keyboard or arrow) executing search...$searchQuery")
                viewModel.setQuery(searchQuery).let {
                    onWorkOrderSearchOrFilter()
                }
            }
            true
        }
        //search button clicked
        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG,"SearchView: (button) executing search...$searchQuery")
            viewModel.setQuery(searchQuery).let {
                onWorkOrderSearchOrFilter()
            }
        }
    }



    private fun initRecyclerView(){
        work_order_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@WorkOrderFragment.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)
            recyclerAdapter = WorkOrderListAdapter(interaction = this@WorkOrderFragment)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if(lastPosition == recyclerAdapter.itemCount.minus(1)){
                        Log.d(TAG, "WorkOrderFragment: Attempting to load next page...")
                            viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clear references - can leak memory
        work_order_recyclerview.adapter = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_filter_settings -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(position: Int, item: WorkOrder) {
        viewModel.setWorkOrder(item)
        findNavController().navigate(R.id.action_workorderFragment_to_viewWorkorderFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.workOrderFields?.layoutManagerState?.let { lmState ->
            work_order_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onRefresh() {
        onWorkOrderSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog(){

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_work_order_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilter()
            val order = viewModel.getOrder()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    WORK_ORDER_FILTER_CREATED_AT -> check(R.id.filter_created_at)
                    WORK_ORDER_FILTER_CREATED_BY -> check(R.id.filter_created_by)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    WORK_ORDER_ORDER_ASC -> check(R.id.filter_asc)
                    WORK_ORDER_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_created_by -> WORK_ORDER_FILTER_CREATED_BY
                        R.id.filter_created_at -> WORK_ORDER_FILTER_CREATED_AT
                        else -> WORK_ORDER_FILTER_CREATED_AT
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptions(newFilter, newOrder)
                    setWorkOrderFilter(newFilter)
                    setWorkOrderOrder(newOrder)
                }

                onWorkOrderSearchOrFilter()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}