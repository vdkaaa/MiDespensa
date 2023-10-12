package cl.dsantander.midespensa2023

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private val productos = ArrayList<Producto>() // Lista de productos
    private lateinit var adapter: CustomAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Configurar el adaptador
        adapter = CustomAdapter(productos)
        recyclerView.adapter = adapter

        // Agregar productos iniciales a la lista
        agregarProductosIniciales()

        // Botón para agregar un producto
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
            val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
            val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
            val etCantidad = dialogView.findViewById<EditText>(R.id.etCantidad)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Agregar Producto")
                .setView(dialogView)
                .setPositiveButton("Agregar") { _, _ ->
                    val nombreProducto = etNombre.text.toString()
                    val descripcionProducto = etDescripcion.text.toString()
                    val cantidadProducto = etCantidad.text.toString().toIntOrNull() ?: 0

                    if (nombreProducto.isNotEmpty() && cantidadProducto > 0) {
                        val nuevoProducto = Producto(nombreProducto, descripcionProducto, R.drawable.ic_launcher_foreground, cantidadProducto)
                        productos.add(nuevoProducto)
                        adapter.notifyItemInserted(productos.size - 1)
                        Log.d("MiDespensa", "Tamaño de productos: ${productos.size}")
                    } else {
                        // Manejar un caso de entrada no válida
                    }
                }
                .setNegativeButton("Cancelar", null)
                .create()
            dialog.show()
        }

        // Botón para eliminar un producto
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        btnEliminar.setOnClickListener {
            if (productos.isNotEmpty()) {
                productos.removeAt(productos.size - 1)
                adapter.notifyItemRemoved(productos.size)
            }
        }

        CustomAdapter.onItemClick = {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("Producto",it)
            startActivity(intent)
        }
    }


    private fun obtenerCantidadProducto(producto: Producto): Int {
        return sharedPreferences.getInt(producto.nombre, 0)
    }

    private fun guardarCantidadProducto(producto: Producto) {
        val editor = sharedPreferences.edit()
        editor.putInt(producto.nombre, producto.cantidad)
        editor.apply()
    }

    private fun agregarProductosIniciales() {
        // Agregar productos iniciales a la lista
        val producto1 = Producto("Producto 1", "Descripción del Producto 1", R.drawable.ic_launcher_foreground, 4)
        val producto2 = Producto("Producto 2", "Descripción del Producto 2", R.drawable.ic_launcher_foreground,2)
        val producto3 = Producto("Producto 3", "Descripción del Producto 3", R.drawable.ic_launcher_foreground,4)

        productos.add(producto1)
        productos.add(producto2)
        productos.add(producto3)

        // Notificar al adaptador sobre los productos iniciales
        adapter.notifyDataSetChanged()

        Log.d("MiDespensa", "Número de productos: ${productos.size}")
    }


}


