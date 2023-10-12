package cl.dsantander.midespensa2023
import android.os.Parcel
import android.os.Parcelable

data class Usuario(
    val nombre: String,
    val correoElectronico: String,
    val contrasena: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(correoElectronico)
        parcel.writeString(contrasena)
    }
    fun verificaCredenciales(email: String, password: String): Boolean {
        return correoElectronico == email && contrasena == password
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuario> {
        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }
    }
}
