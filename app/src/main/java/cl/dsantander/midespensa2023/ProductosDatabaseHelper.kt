package cl.dsantander.midespensa2023
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductosDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "productos_database"
        const val DATABASE_VERSION = 1
        const val TABLE_PRODUCTOS = "productos_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_CANTIDAD = "cantidad"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_MARCA = "marca"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_PRODUCTOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_DESCRIPCION TEXT,
                $COLUMN_CANTIDAD INTEGER,
                $COLUMN_PRECIO FLOAT,
                $COLUMN_TIPO TEXT,
                $COLUMN_MARCA TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    @SuppressLint("Range")
    fun readProductos(): List<Producto> {
        val productos = ArrayList<Producto>()
        val db = readableDatabase
        val columns = arrayOf(
            COLUMN_NOMBRE,
            COLUMN_DESCRIPCION,
            COLUMN_CANTIDAD,
            COLUMN_PRECIO,
            COLUMN_TIPO,
            COLUMN_MARCA
        )
        val cursor: Cursor = db.query(TABLE_PRODUCTOS, columns, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
            val descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPCION))
            val cantidad = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD))
            val precio = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRECIO))
            val tipoString = cursor.getString(cursor.getColumnIndex(COLUMN_TIPO))
            val tipo = TipoProducto.valueOf(tipoString)
            val marca = cursor.getString(cursor.getColumnIndex(COLUMN_MARCA))

            val producto = Producto(nombre, descripcion, R.drawable.ic_launcher_foreground, cantidad, precio, tipo, marca)
            productos.add(producto)
        }

        cursor.close()
        db.close()

        return productos
    }

    fun insertProducto(producto: Producto) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, producto.nombre)
        values.put(COLUMN_DESCRIPCION, producto.detalle)
        values.put(COLUMN_CANTIDAD, producto.cantidad)
        values.put(COLUMN_PRECIO, producto.precio)
        values.put(COLUMN_TIPO, producto.tipo.name) // Convertir la enumeración a String
        values.put(COLUMN_MARCA, producto.marca)
        db.insert(TABLE_PRODUCTOS, null, values)
        db.close()
    }

    fun updateProducto(producto: Producto) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_CANTIDAD, producto.cantidad)
        values.put(COLUMN_PRECIO, producto.precio)
        values.put(COLUMN_TIPO, producto.tipo.name) // Convertir la enumeración a String

        db.update(TABLE_PRODUCTOS, values, "$COLUMN_NOMBRE = ?", arrayOf(producto.nombre))

        db.close()
    }
    fun deleteProducto(producto: Producto) {
        val db = writableDatabase
        db.delete(TABLE_PRODUCTOS, "$COLUMN_NOMBRE = ?", arrayOf(producto.nombre))
        db.close()
    }
}
