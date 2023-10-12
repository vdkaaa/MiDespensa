package cl.dsantander.midespensa2023


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class CustomAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<CustomAdapter.ProductViewHolder>() {


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productos[position]
        holder.itemTitle.text = producto.nombre // Asigna el nombre del producto
        holder.itemDetail.text = producto.detalle // Asigna los detalles del producto
        holder.itemImage.setImageResource(producto.imagen) // Asigna la imagen del producto

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(producto)
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    companion object {
        var onItemClick: ((Producto)->Unit)? = null
    }


}


