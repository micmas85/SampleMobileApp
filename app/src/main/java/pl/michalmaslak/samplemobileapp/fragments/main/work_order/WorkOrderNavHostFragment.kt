package pl.michalmaslak.samplemobileapp.fragments.main.work_order

import android.content.Context
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import pl.michalmaslak.samplemobileapp.ui.main.MainActivity

class WorkOrderNavHostFragment : NavHostFragment(){

    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory =
            (activity as MainActivity).workOrderFragmentFactory
        super.onAttach(context)
    }

    companion object{

        const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"

        @JvmStatic
        fun create(
            @NavigationRes graphId: Int = 0
        ): WorkOrderNavHostFragment {
            var bundle: Bundle? = null
            if(graphId != 0){
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result =
                WorkOrderNavHostFragment()
            if(bundle != null){
                result.arguments = bundle
            }
            return result
        }
    }
}