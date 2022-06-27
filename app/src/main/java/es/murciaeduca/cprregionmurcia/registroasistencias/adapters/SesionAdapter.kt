package es.murciaeduca.cprregionmurcia.registroasistencias.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion

class SesionAdapter(
    private val list: List<Sesion>
) : RecyclerView.Adapter<SesionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.sesion_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.actividadTitulo.text = list[position].actividad_codigo
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val actividadTitulo: TextView

        init {
            actividadTitulo = view.findViewById(R.id.actividadTitle)
        }
    }
}



