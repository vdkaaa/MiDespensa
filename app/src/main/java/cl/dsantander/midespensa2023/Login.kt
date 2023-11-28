package cl.dsantander.midespensa2023

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class Login : AppCompatActivity() {
    companion object {
        // Declarar la lista de usuarios como propiedad de clase para que sea accesible desde otras actividades.
        val listaUsuarios = mutableListOf<Usuario>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        listaUsuarios.add(Usuario("Usuario1", "usuario1", "demo1"))
        listaUsuarios.add(Usuario("Usuario2", "usuario2", "demo2"))

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister =  findViewById<Button>(R.id.buttonRegister)
        val btn_es = findViewById<ImageButton>(R.id.btn_es)
        val btn_en = findViewById<ImageButton>(R.id.btn_en)

        // Agregar clic listeners para cambiar el idioma
        btn_es.setOnClickListener {
            setLocale("es")
        }

        btn_en.setOnClickListener {
            setLocale("en")
        }

        buttonRegister.setOnClickListener {

            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()


            val usuario = listaUsuarios.find { it.verificaCredenciales(email, password) }

            if (usuario != null) {
                // Credenciales válidas, inicia sesión y redirige al usuario a la pantalla principal.
                val intent = Intent(this, OrdenProductosActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Credenciales inválidas, muestra un mensaje de error.
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)

        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )

        // Reiniciar la actividad para aplicar el cambio de idioma
        recreate()
    }
}
