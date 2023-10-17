package cl.dsantander.midespensa2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LugaresInteresActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val lugaresInteres = ArrayList<LugarInteres>() // Lista de lugares de interés
    private lateinit var adapter: LugaresInteresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lugares_interes)

        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerViewLugares)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Configurar el adaptador
        adapter = LugaresInteresAdapter(lugaresInteres)
        recyclerView.adapter = adapter

        // Agregar lugares de interés iniciales
        agregarLugaresInteresIniciales()
    }

    private fun agregarLugaresInteresIniciales() {
        // Agregar lugares de interés iniciales a la lista
        val lugar1 = LugarInteres("Lider Talca", "Compra desde casa en Supermercado y Mundo Lider", R.drawable.lider_image, 42.3601, -71.0589, " Dos Nte. 1422, 3480094 Talca, Maule")
        val lugar2 = LugarInteres("Jumbo", "No te pierdas las ofertas del día de Jumbo.cl! Compra online alimentos, productos de limpieza, hogar y más y recíbelo en tu puerta.", R.drawable.jumbo_image, 42.3599, -71.0568, "El Arenal 411, Talca, Maule")
        val lugar3 = LugarInteres("Holey", "Holy Market Las Heras", R.drawable.holey_image, 42.3612, -71.0576, "5 Ote. 1796, 3461407 Talca, Maule")


        lugaresInteres.add(lugar1)
        lugaresInteres.add(lugar2)
        lugaresInteres.add(lugar3)


        // Notificar al adaptador sobre los lugares de interés iniciales
        adapter.notifyDataSetChanged()
    }
}
