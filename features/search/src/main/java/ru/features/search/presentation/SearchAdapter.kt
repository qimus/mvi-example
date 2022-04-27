package ru.features.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.features.search.databinding.RvSearchGithubItemBinding
import ru.mvi.domain.model.GithubSearchItem

class DiffCallback : DiffUtil.ItemCallback<GithubSearchItem>() {
    override fun areItemsTheSame(oldItem: GithubSearchItem, newItem: GithubSearchItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GithubSearchItem, newItem: GithubSearchItem): Boolean {
        return oldItem == newItem
    }
}

class SearchAdapter(
    private val onDetailsClick: (GithubSearchItem) -> Unit
) : ListAdapter<GithubSearchItem, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvSearchGithubItemBinding.inflate(inflater, parent, false)

        return GithubItemViewHolder(binding, onDetailsClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GithubItemViewHolder).bind(getItem(position))
    }

    class GithubItemViewHolder(
        private val binding: RvSearchGithubItemBinding,
        private val onDetailsClick: (GithubSearchItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GithubSearchItem) {
            binding.root.setOnClickListener { onDetailsClick(item) }
            Glide.with(binding.ivAvatar).load(item.owner.avatarUrl).into(binding.ivAvatar)
            binding.tvName.text = item.name
            binding.tvDescription.text = item.fullName
            binding.tvStarCount.text = item.starsCount.toString()
            binding.tvForkCount.text = item.forksCount.toString()
        }
    }
}
