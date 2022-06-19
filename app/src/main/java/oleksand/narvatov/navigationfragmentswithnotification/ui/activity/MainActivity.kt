package oleksand.narvatov.navigationfragmentswithnotification.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import oleksand.narvatov.navigationfragmentswithnotification.R
import oleksand.narvatov.navigationfragmentswithnotification.data.storage.SharedPrefFragmentsStorage
import oleksand.narvatov.navigationfragmentswithnotification.databinding.ActivityMainBinding
import oleksand.narvatov.navigationfragmentswithnotification.entity.FragmentAction
import oleksand.narvatov.navigationfragmentswithnotification.ui.adapter.FragmentAdapter
import oleksand.narvatov.navigationfragmentswithnotification.ui.fragment.NotificationCounterFragment
import oleksand.narvatov.navigationfragmentswithnotification.ui.viewmodel.SharedViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    private val sharedViewModel: SharedViewModel by viewModels()

    private val fragmentAdapter by lazy { FragmentAdapter(this) }

    @Inject
    lateinit var sharedPrefFragmentsStorage: SharedPrefFragmentsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModels()
        initViews()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val selectedPage = intent?.getIntExtra(SELECTED_PAGE, DEFAULT_SELECTED_PAGE) ?: DEFAULT_SELECTED_PAGE
        binding.viewPager.setCurrentItem(selectedPage.minus(1), true)
    }

    private fun initViewModels() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.fragmentActionStateFlow.collectLatest { action ->
                when(action) {
                    is FragmentAction.CreateBunchOfFragments -> {
                        fragmentAdapter.addFragments(action.count)
                    }
                    is FragmentAction.CreateNextFragment -> {
                        fragmentAdapter.addFragment(action.page)

                        binding.viewPager.currentItem = action.page.minus(1)
                    }
                    FragmentAction.DeleteFragment -> {
                        fragmentAdapter.removeLastFragment()
                    }
                }
            }
        }

        val fragmentsCount = sharedPrefFragmentsStorage.loadFragments()
        sharedViewModel.sendFragmentAction(FragmentAction.CreateBunchOfFragments(fragmentsCount))
    }

    private fun initViews() {
        with(binding.viewPager) {
            adapter = fragmentAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    override fun onStop() {
        super.onStop()
        sharedPrefFragmentsStorage.storeFragments(fragmentAdapter.itemCount)
    }

    companion object {
        const val SELECTED_PAGE = "SELECTED_PAGE"
        private const val DEFAULT_SELECTED_PAGE = 1
    }

}