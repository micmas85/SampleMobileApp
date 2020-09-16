package pl.michalmaslak.samplemobileapp.ui.main.create_workorder

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

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseCreateWorkOrderFragment
constructor(
    @LayoutRes
    private val layoutRes: Int,
    private val viewModelFactory: ViewModelProvider.Factory

) : Fragment(layoutRes) {

    val TAG: String = "AppDebug"

    val viewModel: CreateWorkOrderViewModel by viewModels{
        viewModelFactory
    }

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.createWorkorderFragment, activity as AppCompatActivity)
        setupChannel()

    }

    private fun setupChannel() = viewModel.setupChannel()

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