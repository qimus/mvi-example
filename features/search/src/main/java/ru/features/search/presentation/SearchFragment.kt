package ru.features.search.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.features.search.R
import ru.features.search.databinding.FragmentSearchBinding
import ru.features.search.di.injector
import ru.features.search.nav.SearchNavigation
import ru.mvi.core.TextWatcherAdapter
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.collectFlow
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override val layoutInflater: LayoutInflateMethod = FragmentSearchBinding::inflate

    lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    @Inject
    lateinit var navigation: SearchNavigation

    lateinit var rvAdapter: SearchAdapter

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        searchViewModel =
            ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        rvAdapter = SearchAdapter {
            navigation.navigateToDetails(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.m_profile -> {
                navigation.navigateToProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun configure(binding: FragmentSearchBinding) {
        super.configure(binding)

        binding.bindState(
            searchViewModel.state,
            searchViewModel::setEvent
        )

        bindEffects(searchViewModel.effect)
    }

    private fun FragmentSearchBinding.bindState(
        uiState: Flow<SearchState>,
        uiActions: (UiEvent) -> Unit
    ) {
        rvItems.adapter = rvAdapter

        setupScrollListener(uiActions)

        etSearch.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable) {
                uiActions(UiEvent.Search(s.toString()))
            }
        })

        collectFlow(searchViewModel.state.map { it.search }.distinctUntilChanged()) {
            etSearch.setText(it)
            etSearch.setSelection(it.length)
        }

        collectFlow(uiState) { state ->
            showProgress(state.isFetching)
            showEmptyMessage(state)
            rvAdapter.submitList(state.items)
        }
    }

    private fun FragmentSearchBinding.setupScrollListener(
        onScrollChanged: (UiEvent.Scroll) -> Unit
    ) {
        val layoutManager = rvItems.layoutManager as LinearLayoutManager
        rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UiEvent.Scroll(
                        totalItemCount = totalItemCount,
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisiblePosition
                    )
                )
            }
        })
    }

    private fun bindEffects(effectFlow: Flow<UiEffect>) {
        collectFlow(effectFlow) {
            when (it) {
                is UiEffect.ShowToast -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showProgress(isShow: Boolean) {
        binding?.progress?.isVisible = isShow
    }

    private fun showEmptyMessage(state: SearchState) {
        binding?.tvEmptyMessage?.isVisible = state.items.isEmpty() && !state.isFetching
        binding?.ivEmptySearch?.isVisible = state.items.isEmpty() && !state.isFetching
        val messageResId = if (state.items.isEmpty() && state.search.isEmpty()) {
            R.string.fragment_search_empty_search_input
        } else {
            R.string.fragment_search_no_result
        }

        binding?.tvEmptyMessage?.text = getString(messageResId)
    }
}
