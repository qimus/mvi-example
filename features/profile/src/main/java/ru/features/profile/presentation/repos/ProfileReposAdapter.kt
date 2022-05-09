package ru.features.profile.presentation.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.features.profile.databinding.RvProfileGithubItemBinding
import ru.mvi.domain.model.GithubRepositoryItem

class DiffCallback : DiffUtil.ItemCallback<GithubRepositoryItem>() {
    override fun areItemsTheSame(oldItem: GithubRepositoryItem, newItem: GithubRepositoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GithubRepositoryItem, newItem: GithubRepositoryItem): Boolean {
        return oldItem == newItem
    }
}

class ProfileReposAdapter(
    private val onDetailsClick: (GithubRepositoryItem) -> Unit
) : ListAdapter<GithubRepositoryItem, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvProfileGithubItemBinding.inflate(inflater, parent, false)

        return GithubItemViewHolder(binding, onDetailsClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GithubItemViewHolder).bind(getItem(position))
    }

    class GithubItemViewHolder(
        private val binding: RvProfileGithubItemBinding,
        private val onDetailsClick: (GithubRepositoryItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GithubRepositoryItem) {
            binding.root.setOnClickListener { onDetailsClick(item) }
            binding.tvName.text = item.name
            binding.tvDescription.text = item.fullName
            binding.tvStarCount.text = item.starsCount.toString()
            binding.tvForkCount.text = item.forksCount.toString()
        }
    }
}
