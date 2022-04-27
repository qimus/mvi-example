package ru.den.detail_info.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ru.den.detail_info.R
import ru.den.detail_info.databinding.FragmentDetailInfoBinding
import ru.den.detail_info.domain.GithubItem
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.asHref
import ru.mvi.core.utils.pretty
import ru.mvi.core.utils.setHtml
import ru.mvi.domain.model.GithubSearchItem
import kotlin.properties.Delegates

class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {
    companion object {
        private const val ARG_MODEL = "argModel"

        fun newInstance(githubItem: GithubSearchItem): DetailInfoFragment {
            return DetailInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MODEL, GithubItem.from(githubItem))
                }
            }
        }
    }

    override val layoutInflater: LayoutInflateMethod = FragmentDetailInfoBinding::inflate

    private var item: GithubItem by Delegates.notNull()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        item = arguments?.getParcelable(ARG_MODEL)
            ?: throw IllegalArgumentException("argModel is empty")
    }

    override fun configure(binding: FragmentDetailInfoBinding) = with(binding) {
        tvName.text = item.fullName
        tvDescription.text = item.description
        Glide.with(binding.root).load(item.owner.avatarUrl).into(ivImage)
        chipFork.text = item.forksCount.pretty()
        chipStart.text = item.starsCount.pretty()
        chipWatch.text = item.watchersCount.pretty()

        tvTagsTitle.isVisible = item.topics.isNotEmpty()
        chipsTags.isVisible = item.topics.isNotEmpty()

        item.topics.forEach {
            chipsTags.addTag(it)
        }

        val homeLink = getString(R.string.feature_details_info_home_page, item.htmlUrl.asHref())
        tvHomeLink.setHtml(homeLink) { uri ->
            Intent(Intent.ACTION_VIEW).apply {
                data = uri
            }.also {
                startActivity(it)
            }
        }
    }

    private fun ChipGroup.addTag(title: String) {
        val item = Chip(requireContext())
        item.text = title
        addView(item)
    }
}
