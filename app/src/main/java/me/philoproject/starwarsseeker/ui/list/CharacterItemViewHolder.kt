package me.philoproject.starwarsseeker.ui.list

import androidx.recyclerview.widget.RecyclerView
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.databinding.CharacterListItemBinding
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel

class CharacterItemViewHolder(private val binding: CharacterListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(characterViewModel: CharacterModel) {
        with(binding) {
            name.text = characterViewModel.name
            gender.text = characterViewModel.gender
            birthYear.text = String.format(root.context.getString(R.string.born_s), characterViewModel.birthYear)
        }
    }
}