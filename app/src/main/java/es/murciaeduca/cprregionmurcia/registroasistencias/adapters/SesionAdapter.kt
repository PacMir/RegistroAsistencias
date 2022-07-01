package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ItemRecyclerSesionBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats

class SesionAdapter(
    private val onItemClicked: (SesionActividad) -> Unit,
) : ListAdapter<SesionActividad, SesionAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SesionActividad>() {
            override fun areItemsTheSame(
                oldItem: SesionActividad,
                newItem: SesionActividad,
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: SesionActividad,
                newItem: SesionActividad,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ItemRecyclerSesionBinding.inflate(
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
        private var binding: ItemRecyclerSesionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: SesionActividad) {
            binding.fechaTitulo.text = AppDateUtil.generateDateInfo(s.inicio, s.fin)
            binding.titulo.text = s.codigo + " " + s.titulo
            binding.participantes.text = "${s.num_asisten}/${s.num_participantes}"
            binding.modalidad.text = s.modalidad
            binding.responsable.text = "Director/a: ${s.responsable}"
            binding.duracion.text = AppDateUtil.duration(s.inicio, s.fin)

            if (s.upload == null) {
                binding.iconCloudDone.visibility = View.GONE
                binding.iconCloudOff.visibility = View.VISIBLE
                binding.iconCloudOff.text = "Datos no enviados"

            } else {
                binding.iconCloudOff.visibility = View.GONE
                binding.iconCloudDone.visibility = View.VISIBLE
                binding.iconCloudDone.text =
                    AppDateUtil.dateToString(s.upload, DateFormats.DATE_TIME.format)
            }
        }
    }
}
