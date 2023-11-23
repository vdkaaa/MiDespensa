package cl.dsantander.midespensa2023

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private val productos = ArrayList<Producto>() // Lista de productos
    private lateinit var adapter: CustomAdapter
    private lateinit var dbHelper: ProductosDatabaseHelper




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = ProductosDatabaseHelper(this)


        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Configurar el adaptador
        adapter = CustomAdapter(productos,dbHelper)
        recyclerView.adapter = adapter

        val filtroTipo = intent.getStringExtra("filtroTipo")
        Log.d("MiDespensa", "Filtro tipo en MainActivity: $filtroTipo")

        Log.d("MiDespensa", "Productos cargados: $productos")
        cargarProductosDesdeDatabase(filtroTipo)

        CustomAdapter.onItemClick = {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("producto", it)
            startActivityForResult(intent, REQUEST_CODE_DETAILED_ACTIVITY)
        }
        Log.d("MiDespensa", "Productos cargados: $productos")
        val producto = intent.getParcelableExtra<Producto>("producto")
        if (producto != null) {
            for (prod in productos) {
                if (prod.nombre == producto.nombre) {
                    prod.cantidad = producto.cantidad
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_product -> {
                // Lógica para manejar la opción "Añadir Producto" en el Toolbar
                mostrarDialogoAgregarProducto()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarProductosDesdeDatabase(filtroTipo: String? = null) {
        println("Filtro tipo: $filtroTipo")

        val tipoProducto: TipoProducto? = filtroTipo?.let {
            Log.d("MiDespensa", "Tipo seleccionado: $it")
            TipoProducto.values().find { tipo -> tipo.name == it }
        }

        val productos = if (tipoProducto != null) {
            dbHelper.readProductosByTipo(tipoProducto)
        } else {
            dbHelper.readProductos()
        }

        println("Productos cargados: $productos")

        this.productos.clear()
        this.productos.addAll(productos)
        adapter.notifyDataSetChanged()
    }



    private fun guardarProductoEnDatabase(producto: Producto) {
        dbHelper.insertProducto(producto)
    }

    private fun mostrarDialogoAgregarProducto() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
        val etCantidad = dialogView.findViewById<EditText>(R.id.etCantidad)
        val etPrecio = dialogView.findViewById<EditText>(R.id.etPrecio)
        val spinnerTipo = dialogView.findViewById<Spinner>(R.id.spinnerTipo)
        val etMarca = dialogView.findViewById<EditText>(R.id.etMarca)

        // Configurar el adaptador para el Spinner con las opciones de tipo de producto
        ArrayAdapter.createFromResource(
            this,
            R.array.tipos_de_producto,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipo.adapter = adapter
        }

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
                val tipoProducto = TipoProducto.valueOf(spinnerTipo.selectedItem.toString())
                val MarcaProducto = etMarca.text.toString()

                // Agregar un nuevo producto a la lista y a la base de datos
                if (nombreProducto.isNotEmpty() && cantidadProducto > 0) {
                    val nuevoProducto = Producto(
                        nombreProducto,
                        descripcionProducto,
                        R.drawable.ic_launcher_foreground,
                        cantidadProducto,
                        precioProducto,
                        tipoProducto,
                        MarcaProducto
                    )
                    productos.add(nuevoProducto)
                    adapter.notifyItemInserted(productos.size - 1)

                    // Guardar el producto en la base de datos
                    guardarProductoEnDatabase(nuevoProducto)
                } else {
                    // Manejar un caso de entrada no válida
                    Toast.makeText(this, "Ingrese datos válidos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILED_ACTIVITY && resultCode == RESULT_OK) {
            // Actualiza la vista del producto en la lista de productos
            val producto = data?.getParcelableExtra<Producto>("producto")
            if (producto != null) {
                for (prod in productos) {
                    if (prod.nombre == producto.nombre) {
                        prod.cantidad = producto.cantidad
                        prod.precio = producto.precio

                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
    companion object {
        const val REQUEST_CODE_DETAILED_ACTIVITY = 1
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}

