package me.philoproject.starwarsseeker.ui.utils

import androidx.recyclerview.widget.DiffUtil
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel

/**
 * DiffUtil for the Character List Adapter. Compares models to notify when items are removed, added, or changed.
 * Animates list updates.
 */
object CharacterDiffUtil : DiffUtil.ItemCallback<CharacterModel>() {
    override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
        return oldItem.equalsOther(newItem)
    }
}