package cl.dsantander.midespensa2023

import ProductosDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MovimientosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        // Obtener los movimientos desde la base de datos
        val movimientos = obtenerMovimientosDesdeBD()

        // Obtener el ListView desde el diseño
        val listView: ListView = findViewById(R.id.listViewMovimientos)

        // Crear un ArrayAdapter para los movimientos
        val adapter = ArrayAdapter(this, R.layout.list_item_movimiento, R.id.textViewMovimiento, movimientos)

        // Asignar el adaptador al ListView
        listView.adapter = adapter
        // Obtener el botón desde el diseño
        val btnBackToMain: Button = findViewById(R.id.btnBackToMain)

        // Agregar un listener al botón para manejar el clic
        btnBackToMain.setOnClickListener {
            // Regresar a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtenerMovimientosDesdeBD(): List<String> {
        // Aquí deberías obtener los movimientos reales desde la base de datos
        // y devolver una lista de cadenas representando cada movimiento.
        // Estas cadenas se mostrarán en el ListView.
        val dbHelper = ProductosDatabaseHelper(this)
        val movimientos = dbHelper.readAllMovimientos()

        val movimientosStringList = mutableListOf<String>()
        for (movimiento in movimientos) {
            // Personaliza cómo deseas mostrar cada movimiento en la lista
            val movimientoString = "ID: ${movimiento.id}, Nombre del Producto: ${movimiento.productoId}, Tipo: ${movimiento.tipoMovimiento}, Fecha: ${movimiento.fechaMovimiento}"
            movimientosStringList.add(movimientoString)
        }

        return movimientosStringList
    }
}