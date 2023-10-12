package cl.dsantander.midespensa2023

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Register: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Crea una instancia de Usuario con los datos proporcionados por el usuario.
            val usuario = Usuario(nombre, email, password)

            // Agrega el usuario a la lista de usuarios en la actividad de registro.
            Login.listaUsuarios.add(usuario) // Accede a la lista de usuarios de LoginActivity y agrega el usuario

            // Pasa el objeto Usuario a la siguiente actividad.
            val intent = Intent(this, Login::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }
    }
}
