package cl.dsantander.midespensa2023


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class CustomAdapter(private val productos: MutableList<Producto>, private val dbHelper: ProductosDatabaseHelper) : RecyclerView.Adapter<CustomAdapter.ProductViewHolder>() {


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var itemcantidad: TextView = itemView.findViewById(R.id.cantidadTextView)
        var btnEliminarProducto: Button = itemView.findViewById(R.id.btnEliminarProducto)


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
        holder.itemcantidad.text =  "Cantidad: " + producto.cantidad.toString()//Asigna la cantidad de los productos

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(producto)
        }
        // Agregar click listener para el botón "Eliminar Producto"
        holder.btnEliminarProducto.setOnClickListener {
            // Eliminar el producto de la lista en memoria
            productos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productos.size)

            // Eliminar el producto de la base de datos
            dbHelper.deleteProducto(producto)
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    companion object {
        var onItemClick: ((Producto)->Unit)? = null
    }




}


