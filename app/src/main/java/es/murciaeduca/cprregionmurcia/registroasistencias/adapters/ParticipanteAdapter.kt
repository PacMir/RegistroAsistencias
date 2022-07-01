package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ItemRecyclerParticipanteBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats

class ParticipanteAdapter(
    private val onItemClicked: (ParticipanteAsistencia) -> Unit,
) : ListAdapter<ParticipanteAsistencia, ParticipanteAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ParticipanteAsistencia>() {
            override fun areItemsTheSame(
                oldItem: ParticipanteAsistencia,
                newItem: ParticipanteAsistencia,
            ): Boolean {
                return (oldItem.actividad_codigo == newItem.actividad_codigo && oldItem.nif == oldItem.nif)
            }

            override fun areContentsTheSame(
                oldItem: ParticipanteAsistencia,
                newItem: ParticipanteAsistencia,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ItemRecyclerParticipanteBinding.inflate(
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

    // TODO Implementar databinding
    class ViewHolder(
        private var binding: ItemRecyclerParticipanteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(p: ParticipanteAsistencia) {
            val pos = adapterPosition + 1

            binding.participante.text = pos.toString() + ". ${p.apellidos}, ${p.nombre}"
            binding.nif.text = p.nif

            // Ocultar sección de asistencia
            binding.asisteHora.visibility = View.INVISIBLE
            binding.asisteQr.visibility = View.INVISIBLE
            binding.asisteManual.visibility = View.INVISIBLE

            // Mostrar hora de asistencia y modo de registro
            if (p.asistencia != null) {
                binding.asisteHora.text =
                    "Asiste: " + AppDateUtil.dateToString(p.asistencia, DateFormats.TIME.format) + "h"
                binding.asisteHora.visibility = View.VISIBLE

                if (p.tipo_registro == 1) {
                    binding.asisteQr.visibility = View.VISIBLE
                } else {
                    binding.asisteManual.visibility = View.VISIBLE
                }
            }
        }
    }
}