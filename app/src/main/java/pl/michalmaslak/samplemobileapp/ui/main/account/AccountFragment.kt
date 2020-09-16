package pl.michalmaslak.samplemobileapp.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.models.AccountProperties
import pl.michalmaslak.samplemobileapp.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountStateEvent
import pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountViewState
import pl.michalmaslak.samplemobileapp.util.StateMessageCallback
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AccountFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseAccountFragment(R.layout.fragment_account,  viewModelFactory){



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(ACCOUNT_VIEW_STATE_BUNDLE_KEY,  viewModel.viewState.value)
        super.onSaveInstanceState(outState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //restore state after process death
        savedInstanceState?.let{ inState ->
            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        change_password.setOnClickListener{
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }

        logout_button.setOnClickListener {
            viewModel.logout()
        }
        subscribeObservers()
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.accountProperties?.let {
                    Log.d(TAG, "AccountFragment, ViewState: ${it}")
                    setAccountDataFields(it)
                }
            }

        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

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

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(
            AccountStateEvent.GetAccountPropertiesEvent()
        )
    }

    private fun setAccountDataFields(accountProperties: AccountProperties){
        email?.setText(accountProperties.email)
        username?.setText(accountProperties.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}