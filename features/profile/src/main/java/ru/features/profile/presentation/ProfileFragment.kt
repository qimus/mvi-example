package ru.features.profile.presentation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.features.profile.databinding.FragmentProfileBinding
import ru.features.profile.di.injector
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.collectFlow
import ru.mvi.domain.AccountManager
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override val layoutInflater: LayoutInflateMethod = FragmentProfileBinding::inflate

    @Inject
    lateinit var viewModelFactory: ProfileViewModel.Factory

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun configure(binding: FragmentProfileBinding) = with(binding) {
        btnLogout.setOnClickListener {
            viewModel.logout()
        }

        collectFlow(viewModel.state, ::renderState)
    }

    private fun renderState(state: UiState) {
        val binding = this.binding ?: return
        with(binding) {
            state.user?.let { user ->
                tvName.text = user.login
                tvDescription.text = user.name
                Glide.with(ivImage).load(user.avatar).into(ivImage)
            }
        }
    }
}
