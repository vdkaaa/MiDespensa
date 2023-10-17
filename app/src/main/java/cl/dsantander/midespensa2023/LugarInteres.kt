package cl.dsantander.midespensa2023

data class LugarInteres(
    val nombre: String,
    val descripcion: String,
    val imagenResId: Int,
    val latitud: Double,
    val longitud: Double,
    val direccion: String
)