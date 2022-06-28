package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.SesionItemBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil

class SesionAdapter(
    private val onItemClicked: (SesionActividad) -> Unit
) : ListAdapter<SesionActividad, SesionAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SesionActividad>() {
            override fun areItemsTheSame(
                oldItem: SesionActividad,
                newItem: SesionActividad
            ): Boolean {
                return (oldItem.codigo == newItem.codigo && oldItem.inicio == newItem.inicio)
            }

            override fun areContentsTheSame(
                oldItem: SesionActividad,
                newItem: SesionActividad
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            SesionItemBinding.inflate(
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
        private var binding: SesionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: SesionActividad) {
            binding.titulo.text = s.codigo + " " + s.titulo
            binding.participantes.text = 23.toString()
            binding.modalidad.text = s.modalidad
            binding.responsable.text = "Director/a: " + s.responsable
            binding.inicio.text =
                "Comienzo: " + DateUtil.dateToString(s.inicio, DateFormats.TIME.format) + "h"
            binding.fin.text =
                "Finalización: " + DateUtil.dateToString(s.fin, DateFormats.TIME.format) + "h"
            binding.duracion.text = DateUtil.duration(s.inicio, s.fin)
        }
    }
}
