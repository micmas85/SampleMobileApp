package pl.michalmaslak.samplemobileapp.ui.main.account

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountStateEvent
import pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountViewState
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ChangePasswordFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseAccountFragment(R.layout.fragment_change_password, viewModelFactory){


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

        update_password_button.setOnClickListener {
            viewModel.setStateEvent(
                AccountStateEvent.ChangePasswordEvent(
                    input_current_password.text.toString(),
                    input_new_password.text.toString(),
                    input_confirm_new_password.text.toString()
                )
            )
        }

        subscribeObservers()
    }

    private fun subscribeObservers(){
//        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
//            Log.d(TAG, "ChangePasswordFragment, DataState: ${dataState}")
//            if(dataState != null){
//                stateChangeListener.onDataStateChange(dataState)
//                dataState.data?.let{ data ->
//                    data.response?.let { event ->
//                        if(event.peekContent().message.equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)){
//                            stateChangeListener.hideSoftKeyboard()
//                            findNavController().popBackStack()
//                        }
//                    }
//
//                }
//            }
//        })
    }
}