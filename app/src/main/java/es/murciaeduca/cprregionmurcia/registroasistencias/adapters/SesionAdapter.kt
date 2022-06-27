package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
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
        holder.titulo.text = list[position].codigo + " " + list[position].titulo
        holder.modalidad.text = list[position].modalidad
        holder.responsable.text = "Director/a: " + list[position].responsable
        holder.inicio.text = "Comienzo: " + DateUtil.dateToString(list[position].inicio, DateFormats.HH_mm.format)
        holder.fin.text = "Finalización: " + DateUtil.dateToString(list[position].fin, DateFormats.HH_mm.format)
        holder.duracion.text = DateUtil.duration(list[position].inicio, list[position].fin)
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView
        val modalidad: TextView

        val inicio: TextView
        val fin: TextView
        val duracion: TextView
        val responsable: TextView

        init {
            titulo = view.findViewById(R.id.titulo)
            modalidad = view.findViewById(R.id.modalidad)
            responsable = view.findViewById(R.id.responsable)
            inicio = view.findViewById(R.id.inicio)
            fin = view.findViewById(R.id.fin)
            duracion = view.findViewById(R.id.duracion)
        }
    }
}
