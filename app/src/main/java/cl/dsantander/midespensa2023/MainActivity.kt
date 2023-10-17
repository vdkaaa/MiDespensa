package cl.dsantander.midespensa2023

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private val productos = ArrayList<Producto>() // Lista de productos
    private lateinit var adapter: CustomAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Configurar el adaptador
        adapter = CustomAdapter(productos)
        recyclerView.adapter = adapter
        // Inicializar las SharedPreferences
        sharedPreferences = getSharedPreferences("MiDespensaPrefs", Context.MODE_PRIVATE)

        // Verificar si hay datos guardados en las SharedPreferences
        val productosGuardados = sharedPreferences.getString("productos", null)

        if (productosGuardados != null) {
            // Si hay datos guardados, conviértelos desde JSON a una lista de productos
            val gson = Gson()
            val tipoLista = object : TypeToken<List<Producto>>() {}.type
            productos.addAll(gson.fromJson(productosGuardados, tipoLista))
        } else {
            // Si no hay datos guardados, agrega productos iniciales
            agregarProductosIniciales()
        }

        // Botón para agregar un producto
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
            val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
            val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
            val etCantidad = dialogView.findViewById<EditText>(R.id.etCantidad)
            val etPrecio =dialogView.findViewById<EditText>(R.id.etPrecio)
            val etTipo = dialogView.findViewById<EditText>(R.id.etTipo)
            val etMarca= dialogView.findViewById<EditText>(R.id.etMarca)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Agregar Producto")
                .setView(dialogView)
                .setPositiveButton("Agregar") { _, _ ->
                    val nombreProducto = etNombre.text.toString()
                    val descripcionProducto = etDescripcion.text.toString()
                    val cantidadProducto = etCantidad.text.toString().toIntOrNull() ?: 0
                    val precioProductoStr = etPrecio.text.toString()
                    val precioProducto = if (precioProductoStr.isNotEmpty()) {
                        precioProductoStr.toFloatOrNull() ?: 0.0f
                    } else {
                        0.0f
                    }
                    val tipoProducto = etTipo.text.toString()
                    val MarcaProducto = etMarca.text.toString()

                    // Agregar un nuevo producto a la lista
                    if (nombreProducto.isNotEmpty() && cantidadProducto > 0) {
                        val nuevoProducto = Producto(nombreProducto, descripcionProducto, R.drawable.ic_launcher_foreground, cantidadProducto, precioProducto, tipoProducto, MarcaProducto)
                        productos.add(nuevoProducto)
                        adapter.notifyItemInserted(productos.size - 1)
                        Log.d("MiDespensa", "Tamaño de productos: ${productos.size}")

                        // Guardar los productos actualizados en las SharedPreferences
                        val gson = Gson()
                        val productosJson = gson.toJson(productos)
                        val editor = sharedPreferences.edit()
                        editor.putString("productos", productosJson)
                        editor.apply()
                    } else {
                        // Manejar un caso de entrada no válida
                    }
                }
                .setNegativeButton("Cancelar", null)
                .create()
            dialog.show()
        }

        // Botón para abrir la ComprarActivity
        val btnAbrirComprar = findViewById<Button>(R.id.botonListaComprar)
        btnAbrirComprar.setOnClickListener {
            val intent = Intent(this, ComprarActivity::class.java)
            intent.putParcelableArrayListExtra("productos", ArrayList(productos))
            startActivity(intent)
        }



        CustomAdapter.onItemClick = {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("producto",it)
            finish()
            startActivity(intent)
        }

        val btnComprar = findViewById<Button>(R.id.botonListaComprar)
        btnComprar.setOnClickListener {
            // Crear un Intent para abrir la otra actividad (Reemplaza "OtraActivity" por el nombre de tu otra actividad)
            val intent = Intent(this, ComprarActivity::class.java)
            // Agregar la lista productos al Intent
            intent.putParcelableArrayListExtra("listaProductos", productos)
            // Iniciar la otra actividad
            startActivity(intent)
        }


        val producto = intent.getParcelableExtra<Producto>("producto")
        if(producto!=null){
            for (prod in productos) {
                if(prod.nombre == producto.nombre){
                    prod.cantidad= producto.cantidad
                }
            }
        }

        // Verificar si se pasaron productosComprados desde ComprarActivity
        val productosComprados = intent.getParcelableArrayListExtra<Producto>("productosComprados")

        if (productosComprados != null) {
            for (productoComprado in productosComprados) {
                // Verificar si la cantidad es mayor que 0
                if (productoComprado.cantidad > 0) {
                    val existingProduct = findProductByName(productoComprado.nombre)
                    if (existingProduct != null) {
                        // Si el producto ya existe, actualiza la cantidad
                        existingProduct.cantidad += productoComprado.cantidad
                    } else {
                        // Si el producto no existe y tiene una cantidad mayor que 0, agrégalo a la lista
                        productos.add(productoComprado)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }

    }
    private fun findProductByName(productName: String): Producto? {
        return productos.find { it.nombre == productName }
    }

    private fun agregarProductosIniciales() {
        // Agregar productos iniciales a la lista
        val producto1 = Producto("Pan", "Pan amasado", R.drawable.pan_image, 4,1000.0f,"Comida","-")
        val producto2 = Producto("Tomate", "Tomate Ariqueño", R.drawable.tomate_image,2,600.0f,"Verdura","-")
        val producto3 = Producto("Avena Instantanea", "Avena instantanea de 700gr", R.drawable.avena_image,4,1000.0f,"Avena","Quaker")

        productos.add(producto1)
        productos.add(producto2)
        productos.add(producto3)

        // Notificar al adaptador sobre los productos iniciales
        adapter.notifyDataSetChanged()

        Log.d("MiDespensa", "Número de productos: ${productos.size}")
    }
}

