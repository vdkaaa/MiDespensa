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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ComprarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar)

        val listaProductos: ArrayList<Producto>? = intent.getParcelableArrayListExtra("listaProductos")

        if (listaProductos != null) {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager

            // Configurar el adaptador del RecyclerView
            val adapter = ListCompraAdapter(listaProductos)
            recyclerView.adapter = adapter
        }
    }
}
