package ru.features.profile.presentation.repos

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.features.profile.databinding.FragmentReposBinding
import ru.features.profile.di.injector
import ru.features.profile.nav.ProfileNavigation
import ru.mvi.core.navigation.Router
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.collectFlow
import ru.mvi.domain.repository.SortDirection
import javax.inject.Inject

class ReposFragment : BaseFragment<FragmentReposBinding>() {
    companion object {
        private const val ARG_LOGIN = "argLogin"

        fun newInstance(login: String): ReposFragment = ReposFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_LOGIN, login)
            }
        }
    }

    override val layoutInflater: LayoutInflateMethod = FragmentReposBinding::inflate

    @Inject
    lateinit var viewModelFactory: ReposViewModel.Factory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigation: ProfileNavigation

    private val viewModel: ReposViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ReposViewModel::class.java]
    }

    lateinit var login: String

    lateinit var adapter: ProfileReposAdapter

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            login = args.getString(ARG_LOGIN, "")
        }
        adapter = ProfileReposAdapter {
            navigation.navigateToDetailsInfo(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.setEvent(ReposUiEvent.FetchRepos(login))
        }

        binding?.rvRepos?.adapter = adapter

        collectFlow(viewModel.state, ::renderState)
        collectFlow(viewModel.effect, ::renderEffect)
        collectFlow(viewModel.state.map { it.sort }.distinctUntilChanged(), ::renderSort)
    }

    override fun configure(binding: FragmentReposBinding) = with(binding) {
        configureSortDirection()
        configureSortFields()
    }

    private fun renderEffect(effect: ReposUiEffect) {
        when (effect) {
            is ReposUiEffect.ScrollToTop -> {
                val binding = this.binding ?: return
                binding.root.postDelayed({
                    binding.rvRepos.smoothScrollToPosition(0)
                }, 500)
            }
            is ReposUiEffect.ShowError -> {
                context?.let {
                    Toast.makeText(it, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderSort(sort: SortOptions) {
        val binding = this.binding ?: return
        for (i in 0 until binding.sortField.count) {
            val sortFieldDto = binding.sortField.adapter.getItem(i) as SortFieldDto
            if (sortFieldDto.value == sort.field) {
                binding.sortField.setSelection(i)
                break
            }
        }
    }

    private fun renderState(state: ReposUiState) {
        adapter.submitList(state.repos)
        binding?.progress?.isVisible = state.isFetching
    }

    private fun FragmentReposBinding.configureSortFields() {

        val sortFieldsValues = SortOptions.SORT_FIELDS.map {
            SortFieldDto(root.resources.getString(it.key), it.value)
        }

        val adapter = ArrayAdapter(root.context,
            android.R.layout.simple_spinner_item, sortFieldsValues)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sortField.adapter = adapter

        sortField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, p1: View, position: Int, id: Long) {
                val sortField = adapter.getItemAtPosition(position) as SortFieldDto
                viewModel.setEvent(ReposUiEvent.UpdateSort(field = sortField.value))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun FragmentReposBinding.configureSortDirection() {

        val adapter = ArrayAdapter(root.context,
            android.R.layout.simple_spinner_item, arrayListOf(SortDirection.ASC, SortDirection.DESC)
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sortDir.adapter = adapter

        sortDir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, p1: View, position: Int, id: Long) {
                val sortDir = adapter.getItemAtPosition(position) as SortDirection
                viewModel.setEvent(ReposUiEvent.UpdateSort(direction = sortDir))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private data class SortFieldDto(
        val title: String,
        val value: String
    ) {
        override fun toString(): String = title
    }
}
