package pl.michalmaslak.samplemobileapp.ui.main.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.ui.UICommunicationListener

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseAccountFragment
    constructor(
        @LayoutRes
        private val layoutRes: Int,
        private val viewModelFactory: ViewModelProvider.Factory
    ) : Fragment(layoutRes) {

    val TAG: String = "AppDebug"

    val viewModel: AccountViewModel by viewModels{
        viewModelFactory
    }
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.accountFragment, activity as AppCompatActivity)
        setupChannel()

    }

    private fun setupChannel() = viewModel.setupChannel()

    /*
          @fragmentId is id of fragment from graph to be EXCLUDED from action back bar nav
        */
    fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }
    }

}
