package ru.mvi.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mvi.R
import ru.mvi.appComponent
import ru.mvi.core.navigation.AppNavigator
import ru.mvi.core.navigation.Router
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        if (savedInstanceState == null) {
            viewModel.bootstrap()
        }
    }

    override fun onStart() {
        super.onStart()
        router.setNavigator(AppNavigator(supportFragmentManager, R.id.container))
    }

    override fun onStop() {
        super.onStop()
        router.removeNavigator()
    }

    override fun onBackPressed() {
        if (router.getEntryCount() > 1) {
            router.back()
        } else {
            finish()
        }
    }
}
