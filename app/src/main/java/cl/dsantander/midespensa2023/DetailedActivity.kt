package cl.dsantander.midespensa2023

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val producto = intent.getParcelableExtra<Producto>("producto")
        if (producto != null) {
            val textView: TextView = findViewById(R.id.detailActivityTv)
            val imageView: ImageView = findViewById(R.id.detailActivityIv)
            val detailView: TextView = findViewById(R.id.item_detail)
            val cantidadView: TextView = findViewById(R.id.cantidadTextView)


            textView.text = producto.nombre
            imageView.setImageResource(producto.imagen)
            detailView.text = producto.detalle

            // Obtener la cantidad del producto desde SharedPreferences
            cantidadView.text = "Cantidad: " + producto.cantidad.toString()

            val btnMas: Button = findViewById(R.id.botonCantidadmas)
            val btnMenos: Button = findViewById(R.id.botonCantidadmenos)
            val btnGuardarSalir: Button = findViewById(R.id.botonGuardarSalir)

            btnMas.setOnClickListener {
                // Incrementar la cantidad del producto
                producto.cantidad++;
                cantidadView.text = "Cantidad: " + producto.cantidad.toString()
            }

            btnMenos.setOnClickListener {
                // Disminuir la cantidad del producto
                if (producto.cantidad > 0) {
                    producto.cantidad--
                    cantidadView.text = "Cantidad: " + producto.cantidad.toString()
                }
            }
            btnGuardarSalir.setOnClickListener {
                // Actualizar el producto en la base de datos
                val dbHelper = ProductosDatabaseHelper(this)
                dbHelper.updateProducto(producto)

                // Regresar a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("producto", producto)
                finish()
                startActivity(intent)
            }
        }
    }



}
