package cl.dsantander.midespensa2023

import android.os.Parcel
import android.os.Parcelable

data class Producto(
    val id: Long, // Id
    var nombre: String,
    val detalle: String,
    val imagen: Int,
    var cantidad: Int,
    var precio: Float,
    val tipo: TipoProducto,
    val marca: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        TipoProducto.valueOf(parcel.readString()!!), // Convertir el String a la enumeración TipoProducto
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(nombre)
        parcel.writeString(detalle)
        parcel.writeInt(imagen)
        parcel.writeInt(cantidad)
        parcel.writeFloat(precio)
        parcel.writeString(tipo.name) // Obtener el nombre de la enumeración como String
        parcel.writeString(marca)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto {
            return Producto(parcel)
        }

        override fun newArray(size: Int): Array<Producto?> {
            return arrayOfNulls(size)
        }
    }
}
