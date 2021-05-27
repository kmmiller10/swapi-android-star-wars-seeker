package me.philoproject.starwarsseeker.ui.list

import androidx.recyclerview.widget.RecyclerView
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.databinding.CharacterListItemBinding
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import me.philoproject.starwarsseeker.viewmodels.capitalizeFirstCharacter

/**
 * Binds the CharacterModel for this list item to the View
 */
class CharacterItemViewHolder(private val binding: CharacterListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(characterViewModel: CharacterModel) {
        with(binding) {
            name.text = characterViewModel.name
            gender.text = characterViewModel.gender.capitalizeFirstCharacter()
            birthYear.text = String.format(root.context.getString(R.string.born_s), characterViewModel.birthYear.capitalizeFirstCharacter())
        }
    }
}