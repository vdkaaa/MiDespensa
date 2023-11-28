import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cl.dsantander.midespensa2023.Movimiento
import cl.dsantander.midespensa2023.Producto
import cl.dsantander.midespensa2023.R
import cl.dsantander.midespensa2023.TipoProducto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProductosDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "productos_database"
        const val DATABASE_VERSION = 2
        const val TABLE_PRODUCTOS = "productos_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_CANTIDAD = "cantidad"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_MARCA = "marca"

        const val TABLE_MOVIMIENTOS = "movimientos_table"
        const val COLUMN_MOVIMIENTO_ID = "movimiento_id"
        const val COLUMN_PRODUCTO_ID = "producto_id"
        const val COLUMN_TIPO_MOVIMIENTO = "tipo_movimiento"
        const val COLUMN_FECHA_MOVIMIENTO = "fecha_movimiento"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla de Productos
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

        val createMovimientosTableQuery = """
        CREATE TABLE $TABLE_MOVIMIENTOS (
            $COLUMN_MOVIMIENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_PRODUCTO_ID TEXT,
            $COLUMN_TIPO_MOVIMIENTO TEXT,
            $COLUMN_FECHA_MOVIMIENTO TEXT,
            FOREIGN KEY($COLUMN_PRODUCTO_ID) REFERENCES $TABLE_PRODUCTOS($COLUMN_ID)
        )
    """.trimIndent()

        db?.execSQL(createMovimientosTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Puedes implementar la lógica de actualización si es necesario
    }

    // Función para leer todos los productos de la base de datos
    @SuppressLint("Range")
    fun readProductos(): List<Producto> {
        val productos = ArrayList<Producto>()
        val db = readableDatabase
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_NOMBRE,
            COLUMN_DESCRIPCION,
            COLUMN_CANTIDAD,
            COLUMN_PRECIO,
            COLUMN_TIPO,
            COLUMN_MARCA
        )
        val cursor: Cursor = db.query(TABLE_PRODUCTOS, columns, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
            val descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPCION))
            val cantidad = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD))
            val precio = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRECIO))
            val tipoString = cursor.getString(cursor.getColumnIndex(COLUMN_TIPO))
            val tipo = TipoProducto.valueOf(tipoString)
            val marca = cursor.getString(cursor.getColumnIndex(COLUMN_MARCA))

            val producto = Producto(id,nombre, descripcion, R.drawable.letras_logo, cantidad, precio, tipo, marca)
            productos.add(producto)
        }

        cursor.close()
        db.close()

        return productos
    }



    // Función para insertar un nuevo producto en la base de datos
    fun insertProducto(producto: Producto) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, producto.nombre)
            put(COLUMN_DESCRIPCION, producto.detalle)
            put(COLUMN_CANTIDAD, producto.cantidad)
            put(COLUMN_PRECIO, producto.precio)
            put(COLUMN_TIPO, producto.tipo.name)
            put(COLUMN_MARCA, producto.marca)
        }

        // Generate a unique ID for the new product
        val someGeneratedId = generateUniqueId()
        // Insert the new product with the generated ID
        db.insert(TABLE_PRODUCTOS, null, values)

        // Register the movement in the movements table
        registrarMovimiento(producto.nombre, "AGREGAR", obtenerFechaActual())

        db.close()
    }

    // Function to generate a unique ID (you can adjust the implementation based on your needs)
    private fun generateUniqueId(): String {
        // Generar un UUID (identificador único universal)
        val uniqueId: String = UUID.randomUUID().toString()
        return uniqueId
    }

    // Función para actualizar un producto en la base de datos
    fun updateProducto(producto: Producto, nuevoPrecio: Float) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRECIO, nuevoPrecio) // Cambiar el precio del producto
            put(COLUMN_CANTIDAD, producto.cantidad)
            put(COLUMN_TIPO, producto.tipo.name) // Convertir la enumeración a String
        }

        // Actualizar el producto
        db.update(TABLE_PRODUCTOS, values, "$COLUMN_NOMBRE = ?", arrayOf(producto.nombre))

        // Registrar el movimiento en la tabla de movimientos
        registrarMovimiento(producto.nombre, "ACTUALIZAR", obtenerFechaActual())

        db.close()
    }

    // Función para eliminar un producto de la base de datos
    fun deleteProducto(producto: Producto) {
        val db = writableDatabase
        db.delete(TABLE_PRODUCTOS, "$COLUMN_NOMBRE = ?", arrayOf(producto.nombre))

        // Registrar el movimiento en la tabla de movimientos
        registrarMovimiento(producto.nombre, "ELIMINAR", obtenerFechaActual())

        db.close()
    }

    // Función para leer productos filtrados por tipo
    @SuppressLint("Range")
    fun readProductosByTipo(tipo: TipoProducto): List<Producto> {
        val productos = ArrayList<Producto>()
        val db = readableDatabase
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_NOMBRE,
            COLUMN_DESCRIPCION,
            COLUMN_CANTIDAD,
            COLUMN_PRECIO,
            COLUMN_TIPO,
            COLUMN_MARCA
        )
        val selection = "$COLUMN_TIPO = ?"
        val selectionArgs = arrayOf(tipo.name)
        val cursor: Cursor = db.query(TABLE_PRODUCTOS, columns, selection, selectionArgs, null, null, null)

        try {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                val descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPCION))
                val cantidad = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD))
                val precio = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRECIO))
                val marca = cursor.getString(cursor.getColumnIndex(COLUMN_MARCA))

                val producto = Producto(id,nombre, descripcion, R.drawable.letras_logo, cantidad, precio, tipo, marca)
                productos.add(producto)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return productos
    }



    // Función para registrar un movimiento en la base de datos
    private fun registrarMovimiento(productoNombre: String, tipoMovimiento: String, fechaMovimiento: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCTO_ID, productoNombre)
            put(COLUMN_TIPO_MOVIMIENTO, tipoMovimiento)
            put(COLUMN_FECHA_MOVIMIENTO, fechaMovimiento)
        }

        // Insertar el nuevo movimiento
        db.insert(TABLE_MOVIMIENTOS, null, values)
        db.close()
    }

    // Función para obtener la fecha actual en un formato específico
    private fun obtenerFechaActual(): String {
        // Obtener la fecha actual
        val fechaActual = Date()

        // Definir el formato deseado para la fecha
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        // Formatear la fecha como una cadena y devolverla
        return formato.format(fechaActual)
    }

    @SuppressLint("Range")
    fun readAllMovimientos(): List<Movimiento> {
        val movimientos = ArrayList<Movimiento>()
        val db = readableDatabase
        val columns = arrayOf(
            COLUMN_MOVIMIENTO_ID,
            COLUMN_PRODUCTO_ID,
            COLUMN_TIPO_MOVIMIENTO,
            COLUMN_FECHA_MOVIMIENTO
        )
        val cursor: Cursor = db.query(TABLE_MOVIMIENTOS, columns, null, null, null, null, null)

        try {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_MOVIMIENTO_ID))
                val productoId = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTO_ID))
                val tipoMovimiento = cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_MOVIMIENTO))
                val fechaMovimiento = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_MOVIMIENTO))

                val movimiento = Movimiento(id, productoId, tipoMovimiento, fechaMovimiento)
                movimientos.add(movimiento)
            }
        } finally {
            cursor.close()
            db.close()
        }

        return movimientos
    }

}
