package cl.dsantander.midespensa2023

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListCompraAdapter (private val productos: MutableList<Producto>) : RecyclerView.Adapter<ListCompraAdapter.ProductViewHolder>(){


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var itemcantidad: TextView = itemView.findViewById(R.id.cantidadTextView)
        var btnEliminarProducto: Button = itemView.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ListCompraAdapter.ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productos[position]
        holder.itemTitle.text = producto.nombre // Asigna el nombre del producto
        holder.itemDetail.text = producto.detalle // Asigna los detalles del producto
        holder.itemImage.setImageResource(producto.imagen) // Asigna la imagen del producto
        holder.itemcantidad.text =  "Cantidad: " + producto.cantidad.toString()//Asigna la cantidad de los productos


        // Agregar click listener para el botón "Eliminar Producto"
        holder.btnEliminarProducto.setOnClickListener {
            // Eliminar el producto correspondiente cuando se hace clic en el botón de eliminación
            productos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productos.size)
        }
    }


}