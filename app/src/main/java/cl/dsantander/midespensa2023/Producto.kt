package cl.dsantander.midespensa2023

import android.os.Parcel
import android.os.Parcelable


data class Producto(
    val nombre: String,
    val detalle: String,
    val imagen: Int,// Recurso de imagen (puede ser un Int, R.drawable.xxx)
    var cantidad: Int,
    var precio : Float,
    val tipo: String,
    val marca: String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readString()!!

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(detalle)
        parcel.writeInt(imagen)
        parcel.writeInt(cantidad)
        parcel.writeFloat(precio)
        parcel.writeString(tipo)
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
