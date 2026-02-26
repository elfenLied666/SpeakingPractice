package com.justclient.speakingpractice

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.savedstate.SavedState
import com.justclient.speakingpractice.data.models.MainViewModel
import com.justclient.speakingpractice.databinding.ActivityMainBinding
import kotlin.getValue

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var nowFragment: Int? = null
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = getNavController()
        navController.addOnDestinationChangedListener(this)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.statusBarScrim.updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = insets.top
            }
            view.updatePadding(
                left = insets.left,
                // top = 0, так как мы его обработали "прокладкой"
                right = insets.right,
                bottom = insets.bottom
            )
            windowInsets
        }
        backBtnSetup()
        mainViewModel.checkAndSeedData()
    }

    private fun getNavController(): NavController {
        if (!::navController.isInitialized) {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_settings)
        }
        return navController
    }

    private fun backBtnSetup() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                if (nowFragment != R.id.homeFragment) {
                    navController.navigate(R.id.homeFragment)
                } else {
                    finish() // Повторный вызов для стандартного поведения
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: SavedState?
    ) {
        if (destination.id == R.id.homeFragment) {
            binding.title.text = getString(R.string.ttl1)
            nowFragment = R.id.homeFragment
            binding.backBtn.visibility = View.GONE
        } else if(destination.id == R.id.speakingFragment) {
            binding.title.text = getString(R.string.ttl2)
            nowFragment = R.id.speakingFragment
            binding.backBtn.visibility = View.VISIBLE
        }
    }


}