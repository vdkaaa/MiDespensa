package cl.dsantander.midespensa2023
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class Login : AppCompatActivity() {
    companion object {
        // Declarar la lista de usuarios como propiedad de clase para que sea accesible desde otras actividades.
        val listaUsuarios = mutableListOf<Usuario>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Agrega usuarios a la lista (esto es solo un ejemplo, en una aplicación real, los usuarios se agregarían durante el registro).
        listaUsuarios.add(Usuario("Usuario1", "usuario1", "demo1"))
        listaUsuarios.add(Usuario("Usuario2", "usuario2", "demo2"))

        Log.d("ListaUsuarios", "Lista de Usuarios: $listaUsuarios")
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister =  findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            // Abre la actividad de registro cuando se presiona el botón "Registrarse".
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Verifica las credenciales ingresadas por el usuario utilizando la lista de usuarios actualizada.
            val usuario = listaUsuarios.find { it.verificaCredenciales(email, password) }

            if (usuario != null) {
                // Credenciales válidas, inicia sesión y redirige al usuario a la pantalla principal.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Credenciales inválidas, muestra un mensaje de error.
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
