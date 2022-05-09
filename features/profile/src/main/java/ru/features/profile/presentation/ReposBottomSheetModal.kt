package ru.features.profile.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.features.profile.R
import ru.features.profile.databinding.ModalBottomSheetReposBinding
import ru.features.profile.di.injector
import ru.features.profile.nav.ProfileNavigation
import ru.mvi.core.navigation.AppNavigator
import ru.mvi.core.navigation.Router
import javax.inject.Inject

class ReposBottomSheetModal : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ReposBottomSheetModal"

        private const val ARG_LOGIN = "argLogin"

        fun newInstance(login: String): ReposBottomSheetModal =
            ReposBottomSheetModal().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGIN, login)
                }
            }
    }

    private var binding: ModalBottomSheetReposBinding? = null

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigation: ProfileNavigation

    lateinit var login: String


    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            login = args.getString(ARG_LOGIN, "")
        }

        if (savedInstanceState == null) {
            navigation.navigateToReposList(login)
        }
    }

    override fun onStart() {
        super.onStart()
        router.setNavigator(AppNavigator(
            childFragmentManager, R.id.fragment_container
        ))
    }

    override fun onStop() {
        super.onStop()
        router.removeNavigator()
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ModalBottomSheetReposBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { configure(it) }
    }

    private fun configure(binding: ModalBottomSheetReposBinding) = with(binding) {
        configureBehavior()

    }

    private fun ModalBottomSheetReposBinding.configureBehavior() {
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val lp = bottomSheet.layoutParams
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = lp
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = root.measuredHeight
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset < 0.4) {
                           behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            })
        }
    }
}
