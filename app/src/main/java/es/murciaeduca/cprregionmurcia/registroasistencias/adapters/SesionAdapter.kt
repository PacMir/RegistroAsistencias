package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.SesionItemBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil

class SesionAdapter(
    private val list: List<SesionActividad>,
) : RecyclerView.Adapter<SesionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sesion_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = list[position]

        with(holder) {

            // Click en el elemento
            itemView.setOnClickListener{
                Toast.makeText(itemView.context, s.codigo, Toast.LENGTH_SHORT).show()
            }

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

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = SesionItemBinding.bind(view)
    }
}
