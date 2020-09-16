package pl.michalmaslak.samplemobileapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pl.michalmaslak.samplemobileapp.BaseApplication
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.models.AUTH_TOKEN_BUNDLE_KEY
import pl.michalmaslak.samplemobileapp.models.AuthToken
import pl.michalmaslak.samplemobileapp.ui.BaseActivity
import pl.michalmaslak.samplemobileapp.ui.auth.AuthActivity
import pl.michalmaslak.samplemobileapp.ui.main.account.ChangePasswordFragment
import pl.michalmaslak.samplemobileapp.ui.main.account.UpdateAccountFragment
import pl.michalmaslak.samplemobileapp.ui.main.workorder.ViewWorkOrderFragment
import pl.michalmaslak.samplemobileapp.util.BOTTOM_NAV_BACK_STACK_KEY
import pl.michalmaslak.samplemobileapp.util.BottomNavController
import pl.michalmaslak.samplemobileapp.util.setUpNavigation
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity: BaseActivity(),
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener
{

    @Inject
    @Named("AccountFragmentFactory")
    lateinit var accountFragmentFactory: FragmentFactory

    @Inject
    @Named("CreateWorkOrderFragmentFactory")
    lateinit var createWorkOrderFragmentFactory: FragmentFactory

    @Inject
    @Named("WorkOrderFragmentFactory")
    lateinit var workOrderFragmentFactory: FragmentFactory

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.menu_nav_work_order,
            this
            )
}

override fun onGraphChange() {
    expandAppBar()
}

    override fun onReselectNavItem(
    navController: NavController,
    fragment: Fragment
) {
    when (fragment) {

        is ViewWorkOrderFragment -> {
            navController.navigate(R.id.action_viewWorkorderFragment_to_workorderFragment)
        }

        is UpdateAccountFragment -> {
            navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
        }

        is ChangePasswordFragment -> {
            navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
        }

        else -> {
            // do nothing
        }
    }
}


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        (application as BaseApplication).mainComponent()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavigationView(savedInstanceState)

        restoreSession(savedInstanceState)
        subscribeObservers()
    }

    private fun setupBottomNavigationView(savedInstanceState: Bundle?){
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.onNavigationItemSelected()
        }
        else{
            (savedInstanceState[BOTTOM_NAV_BACK_STACK_KEY] as IntArray?)?.let { items ->
                val backstack = BottomNavController.BackStack()
                backstack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backstack)
            }
        }
    }

    private fun restoreSession(savedInstanceState: Bundle?){
        savedInstanceState?.get(AUTH_TOKEN_BUNDLE_KEY)?.let{ authToken ->
            Log.d(TAG, "restoreSession: Restoring token: ${authToken}")
            sessionManager.setValue(authToken as AuthToken)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // save auth token
        outState.putParcelable(AUTH_TOKEN_BUNDLE_KEY, sessionManager.cachedToken.value)

        // save backstack for bottom nav
        outState.putIntArray(BOTTOM_NAV_BACK_STACK_KEY, bottomNavController.navigationBackStack.toIntArray())
    }

    fun subscribeObservers(){

        sessionManager.cachedToken.observe(this, Observer{ authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if(authToken == null || authToken.account_pk == -1 || authToken.token == null){
                navAuthActivity()
                finish()
            }
        })
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseMainComponent()
    }

    override fun displayProgressBar(bool: Boolean){
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }


}