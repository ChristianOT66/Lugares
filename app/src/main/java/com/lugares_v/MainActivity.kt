package com.lugares_v

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_v.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


//Definimos un objeto para acceder a la autenticación de Firebase
    private lateinit var auth : FirebaseAuth
//Definimos un objeto para acceder a los elementos de la pantalla xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializar la autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Definir el evento onClick del botón Register
        binding.vtRegister.setOnClickListener { haceRegistro() }

        //Definir el evento onClick del botón Register
        binding.vtLogin.setOnClickListener { haceLogin() }
        }

    private fun haceRegistro(){
        //Recupero la información que el usuario escribió en el app
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Utilizo  el objeto auth para hacer el registro...
        auth.createUserWithEmailAndPassword(email,clave).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){  //Si se logró... se creó el usuario
                val user = auth.currentUser
                refresca(user)

            }else{ //Si no se logró hubo un error...
                Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                refresca(null)
            }
        }
    }

    private fun refresca(user: FirebaseUser?) {
        if (user != null) {   //Si hay un usuario entonces paso a la pantalla principal
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)

        }
    }

    private fun haceLogin(){
}}