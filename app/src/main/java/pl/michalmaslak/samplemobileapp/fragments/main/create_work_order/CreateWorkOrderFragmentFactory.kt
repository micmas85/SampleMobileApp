package pl.michalmaslak.samplemobileapp.fragments.main.create_work_order

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import pl.michalmaslak.samplemobileapp.di.main.MainScope
import pl.michalmaslak.samplemobileapp.ui.main.create_workorder.CreateWorkOrderFragment
import javax.inject.Inject


@MainScope
class CreateWorkOrderFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            CreateWorkOrderFragment::class.java.name -> {
                CreateWorkOrderFragment(viewModelFactory)
            }

            else -> {
                CreateWorkOrderFragment(viewModelFactory)
            }
        }


}