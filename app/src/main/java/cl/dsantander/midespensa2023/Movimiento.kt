package cl.dsantander.midespensa2023

data class Movimiento(
    val id: Long,
    val productoId: String,
    val tipoMovimiento: String,
    val fechaMovimiento: String
)