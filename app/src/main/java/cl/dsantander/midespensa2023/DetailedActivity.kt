package cl.dsantander.midespensa2023

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val producto = intent.getParcelableExtra<Producto>("Producto")
        if (producto != null) {
            val textView: TextView = findViewById(R.id.detailActivityTv)
            val imageView: ImageView = findViewById(R.id.detailActivityIv)
            val detailView: TextView = findViewById(R.id.item_detail)
            val cantidadView: TextView = findViewById(R.id.cantidadTextView)

            textView.text = producto.nombre
            imageView.setImageResource(producto.imagen)
            detailView.text = producto.detalle

            // Obtener la cantidad del producto desde SharedPreferences
            val cantidad = obtenerCantidadProducto(producto)
            cantidadView.text = cantidad.toString()

            val btnMas: Button = findViewById(R.id.botonCantidadmas)
            val btnMenos: Button = findViewById(R.id.botonCantidadmenos)

            btnMas.setOnClickListener {
                // Incrementar la cantidad del producto
                val nuevaCantidad = cantidad + 1
                guardarCantidadProducto(producto, nuevaCantidad)
                cantidadView.text = nuevaCantidad.toString()
            }

            btnMenos.setOnClickListener {
                // Disminuir la cantidad del producto
                if (cantidad > 0) {
                    val nuevaCantidad = cantidad - 1
                    guardarCantidadProducto(producto, nuevaCantidad)
                    cantidadView.text = nuevaCantidad.toString()
                }
            }
        }
    }

    private fun obtenerCantidadProducto(producto: Producto): Int {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        // Utiliza el nombre del producto como clave única
        return sharedPreferences.getInt(producto.nombre, 0)
    }

    private fun guardarCantidadProducto(producto: Producto, cantidad: Int) {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Utiliza el nombre del producto como clave única
        editor.putInt(producto.nombre, cantidad)
        editor.apply()
    }
}
