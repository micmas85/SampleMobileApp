package pl.michalmaslak.samplemobileapp.fragments.main.work_order

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.ui.main.workorder.ViewWorkOrderFragment
import pl.michalmaslak.samplemobileapp.ui.main.workorder.WorkOrderFragment
import javax.inject.Inject

@MainScope
class WorkOrderFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            WorkOrderFragment::class.java.name -> {
                WorkOrderFragment(viewModelFactory)
            }

            ViewWorkOrderFragment::class.java.name -> {
                ViewWorkOrderFragment(viewModelFactory)
            }

            else -> {
                WorkOrderFragment(viewModelFactory)
            }
        }


}