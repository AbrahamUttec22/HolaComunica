package net.tecgurus.holacomunicate.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import net.tecgurus.holacomunicate.model.Evento
import kotlinx.android.synthetic.main.list_view_evento.view.*

/**
 * @author aBRAHAM
 */
class EventoAdapter(val context: Context, val layout: Int, val list: List<Evento>) : BaseAdapter() {


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: EventoViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EventoViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EventoViewHolder
        }
        //val fullName = "${list[position].titulo}"
        vh.fecha.text = "${list[position].fecha}" + " " + "${list[position].hora}"
        vh.titulo.text = "${list[position].titulo}"
        vh.description.text = "${list[position].description}"
        Glide
                .with(this.context)
                .load("${list[position].ubicacion}")
                .into(view.imgEvento)
        return view
    }

}

private class EventoViewHolder(view: View) {
    val fecha: TextView = view.textFecha
    val description: TextView = view.textDescription
    val titulo: TextView = view.textTitulo
}