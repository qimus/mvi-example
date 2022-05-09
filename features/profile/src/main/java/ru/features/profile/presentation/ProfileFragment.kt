package ru.features.profile.presentation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.features.profile.databinding.FragmentProfileBinding
import ru.features.profile.di.injector
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.collectFlow
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override val layoutInflater: LayoutInflateMethod = FragmentProfileBinding::inflate

    @Inject
    lateinit var viewModelFactory: ProfileViewModel.Factory

    private var reposBottomModal: ReposBottomSheetModal? = null

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun configure(binding: FragmentProfileBinding) = with(binding) {
        btnLogout.setOnClickListener { viewModel.logout() }
        tvMyRepos.setOnClickListener { viewModel.setEvent(UiEvent.ShowRepos) }

        collectFlow(viewModel.state, ::renderState)
        collectFlow(viewModel.effect, ::handleSideEffect)
    }

    private fun handleSideEffect(effect : UiEffect) {
        when (effect) {
            is UiEffect.ShowRepos -> {
                ReposBottomSheetModal.newInstance(effect.login).also {
                    reposBottomModal = it
                    it.show(parentFragmentManager, ReposBottomSheetModal.TAG)
                }
            }
        }
    }

    private fun renderState(state: UiState) {
        val binding = this.binding ?: return
        with(binding) {
            state.user?.let { user ->
                tvName.text = user.login
                tvDescription.text = user.name
                Glide.with(ivImage).load(user.avatar).circleCrop().into(ivImage)
            }
        }
    }
}
