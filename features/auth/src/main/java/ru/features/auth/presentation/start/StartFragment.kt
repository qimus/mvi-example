package ru.features.auth.presentation.start

import android.content.Context
import android.content.Intent
import ru.features.auth.Configuration
import ru.features.auth.R
import ru.features.auth.databinding.FragmentStartBinding
import ru.features.auth.di.injector
import ru.features.auth.nav.Screens
import ru.mvi.core.navigation.Router
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.asHref
import ru.mvi.core.utils.setHtml
import javax.inject.Inject

class StartFragment : BaseFragment<FragmentStartBinding>() {
    companion object {
        fun newInstance(): StartFragment = StartFragment()
    }

    override val layoutInflater: LayoutInflateMethod = FragmentStartBinding::inflate

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun configure(binding: FragmentStartBinding) = with(binding) {
        btnLogin.setOnClickListener {
            router.navigateTo(Screens.navigateToAuth())
        }

        val detailsInfo = getString(R.string.fragment_start_details_info,
            Configuration.DETAILS_LINK.asHref(null))

        tvDetails.setHtml(detailsInfo) {
            Intent(Intent.ACTION_VIEW).apply {
                data = it
            }.also {
                startActivity(it)
            }
        }
    }
}
