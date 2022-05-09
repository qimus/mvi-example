package ru.features.profile.presentation.repos

import ru.features.profile.R
import ru.mvi.domain.repository.SortDirection

data class SortOptions(
    val field: String,
    val direction: SortDirection = SortDirection.ASC
) {
    companion object {
        const val FIELD_FULL_NAME = "full_name"
        const val FIELD_CREATED = "created"
        const val FIELD_UPDATED = "updated"
        const val FIELD_PUSHED = "pushed"

        val SORT_FIELDS = mapOf(
            R.string.fragment_profile_sort_field_full_name to FIELD_FULL_NAME,
            R.string.fragment_profile_sort_field_created to FIELD_CREATED,
            R.string.fragment_profile_sort_field_updated to FIELD_UPDATED,
            R.string.fragment_profile_sort_field_pushed to FIELD_PUSHED
        )
    }
}
