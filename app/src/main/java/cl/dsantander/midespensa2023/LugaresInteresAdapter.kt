package cl.dsantander.midespensa2023

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class LugaresInteresAdapter(private val lugaresInteres: List<LugarInteres>) :
    RecyclerView.Adapter<LugaresInteresAdapter.LugarViewHolder>() {

    // Define la vista de un elemento de la lista
    class LugarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        var nombreImagen: ImageView = itemView.findViewById(R.id.imagenLugar)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
        val direccionTextView: TextView = itemView.findViewById(R.id.direccionTextView)
        val ubicacionTextView: TextView = itemView.findViewById(R.id.ubicacionTextView)
    }

    // Crea una nueva vista de elemento y la infla
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lugar_interes, parent, false)
        return LugarViewHolder(view)
    }

    // Reemplaza el contenido de la vista
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = lugaresInteres[position]
        holder.nombreTextView.text = lugar.nombre
        holder.descripcionTextView.text = lugar.descripcion
        holder.direccionTextView.text = lugar.direccion
        val ubicacion = "Latitud: ${lugar.latitud}, Longitud: ${lugar.longitud}"
        holder.ubicacionTextView.text = ubicacion
        holder.nombreImagen.setImageResource(lugar.imagenResId) // Asigna la imagen del producto
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount() = lugaresInteres.size
}