package cl.dsantander.midespensa2023

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ComprarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val productosComprados = ArrayList<Producto>() // Lista de productos
    private lateinit var adapter: ListCompraAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar)


        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Configurar el adaptador
        adapter = ListCompraAdapter(productosComprados)
        recyclerView.adapter = adapter
        // Inicializar las SharedPreferences
        sharedPreferences = getSharedPreferences("MisProductosComprados", Context.MODE_PRIVATE)

        // Verificar si hay datos guardados en las SharedPreferences
        val productosInventario = sharedPreferences.getString("productos", null)

        val btnComprar = findViewById<Button>(R.id.btnComprar)
        btnComprar.setOnClickListener {
            // Crear un Intent
            val intent = Intent(this, MainActivity::class.java)
            // Agregar la lista de productos como datos extras
            intent.putParcelableArrayListExtra("productosComprados", productosComprados)
            // Iniciar la actividad MainActivity
            startActivity(intent)
        }

        val btnLugaresInteres = findViewById<Button>(R.id.btnLugares)
        btnLugaresInteres.setOnClickListener {
            // Crear un Intent
            val intent = Intent(this, LugaresInteresActivity::class.java)
            // Iniciar la actividad MainActivity
            startActivity(intent)
        }

            agregarProductosIniciales()
        // Obtener la referencia al TextView del precio total
        val tvPrecioTotalComprar = findViewById<TextView>(R.id.tvPrecioTotal)

        // Calcular el precio total
        val precioTotalComprar = productosComprados.sumByDouble { it.precio.toDouble() * it.cantidad.toDouble() }

        // Formatear el precio total en un formato legible
        val formattedPrecioTotalComprar = String.format("%.2f", precioTotalComprar)

        // Establecer el precio total en el TextView
        tvPrecioTotalComprar.text = "Precio Total: $$formattedPrecioTotalComprar"

    }


    private fun agregarProductosIniciales() {
        // Agregar productos iniciales a la lista
        val producto1 = Producto("Coca-Cola", "Coca-cola en lata 600cc", R.drawable.coca_image, 0, 1200.0f, "Bebida", "Coca-Cola")
        val producto2 = Producto("Pizza Napolita", "Pizza Napolina con tomate y queso extra", R.drawable.pizza_image, 0, 2000.0f, "Comida Rapida", "La Crianza")
        val producto3 = Producto("Leche Cultivada Durazno", "Leche cultivada de 1 litro de Durazno", R.drawable.leche_image, 0, 3000.0f, "Lácteos", "Soprole")
        val producto4 = Producto("Pan", "Pan amasado", R.drawable.pan_image, 0, 1000.0f, "tipo", "marca")


        productosComprados.add(producto1)
        productosComprados.add(producto2)
        productosComprados.add(producto3)
        productosComprados.add(producto4)

        // Notificar al adaptador sobre los productos iniciales
        adapter.notifyDataSetChanged()
    }




}