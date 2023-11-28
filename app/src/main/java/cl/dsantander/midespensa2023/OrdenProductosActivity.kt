package cl.dsantander.midespensa2023

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class OrdenProductosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orden_productos)

        val opciones = resources.getStringArray(R.array.tipos_de_producto)

        val listViewOpciones: ListView = findViewById(R.id.listViewOpciones)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        listViewOpciones.adapter = adapter

        listViewOpciones.setOnItemClickListener { _, _, position, _ ->
            val tipoSeleccionado = opciones[position]
            Log.d("MiDespensa", "Tipo seleccionado: $tipoSeleccionado")

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("filtroTipo", tipoSeleccionado)
            startActivity(intent)
        }
    }
}
