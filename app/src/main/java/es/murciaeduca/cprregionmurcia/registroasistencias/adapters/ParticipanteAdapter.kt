package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ParticipanteActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ParticipanteItemBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil

class ParticipanteAdapter(
    private val onItemClicked: (ParticipanteActividad) -> Unit,
) : ListAdapter<ParticipanteActividad, ParticipanteAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ParticipanteActividad>() {
            override fun areItemsTheSame(
                oldItem: ParticipanteActividad,
                newItem: ParticipanteActividad,
            ): Boolean {
                return (oldItem.actividad_codigo == newItem.actividad_codigo && oldItem.nif == oldItem.nif)
            }

            override fun areContentsTheSame(
                oldItem: ParticipanteActividad,
                newItem: ParticipanteActividad,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ParticipanteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private var binding: ParticipanteItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(p: ParticipanteActividad) {
            val pos = adapterPosition + 1
            binding.participante.text = pos.toString() + ". " + p.apellidos + ", " + p.nombre
            binding.nif.text = p.nif
            binding.asisteQr.text = AppDateUtil.dateToString(p.asiste, DateFormats.TIME.format)
        }
    }
}