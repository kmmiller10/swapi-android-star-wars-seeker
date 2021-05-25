package me.philoproject.starwarsseeker.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import me.philoproject.starwarsseeker.databinding.CharacterListItemBinding
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import me.philoproject.starwarsseeker.ui.utils.CharacterDiffUtil
import me.philoproject.starwarsseeker.ui.utils.OnModelClickListener

class CharacterListAdapter(
    private val characterClickListener: OnModelClickListener
    ) : ListAdapter<CharacterModel, CharacterItemViewHolder>(CharacterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterItemViewHolder(CharacterListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
        holder.itemView.setOnClickListener {
            characterClickListener.onModelItemClicked(character.name)
        }
    }
}