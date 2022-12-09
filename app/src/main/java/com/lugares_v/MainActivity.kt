package com.lugares_v
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api.Client
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.IdTokenListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_v.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient


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
        binding.btRegister.setOnClickListener { haceRegistro() }

        //Definir el evento onClick del botón Register
        binding.btLogin.setOnClickListener { haceLogin() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_r))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btGoogle.setOnClickListener { googleSignIn() }
        }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(cuenta.idToken!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener (this) { task ->
                if (task.isSuccessful) {
                    val user=auth.currentUser
                    refresca(user)
                } else {
                    refresca(null)
                }
            }
    }


    private fun haceRegistro(){
        //Recupero la información que el usuario escribió en el app
        Log.d("Registrandose","Haciendo llamado a creación")
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Utilizo  el objeto auth para hacer el registro...
        auth.createUserWithEmailAndPassword(email,clave).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){  //Si se logró... se creó el usuario
                Log.d("Registrandose","Se registró")
                val user = auth.currentUser
                refresca(user)

            }else{ //Si no se logró hubo un error...
                Log.e("Registrandose","Error de registro")
                Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                refresca(null)
            }
        }
        Log.d("Registrandose","Sale del proceso...")
    }

    private fun refresca(user: FirebaseUser?) {
        if (user != null) {   //Si hay un usuario entonces paso a la pantalla principal
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)

        }
    }

    private fun haceLogin(){

        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        Log.d("Autenticandose","Haciendo llamado de autenticación")
        auth.signInWithEmailAndPassword(email,clave).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){  //Si se logró... se creó el usuario
                Log.d("Autenticado","Se autenticó...")
                val user = auth.currentUser
                refresca(user)

            }else{ //Si no se logró hubo un error...
                Log.e("Autenticado","Error de autenticación")
                Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                refresca(null)
            }
        }
        Log.d("Autenticando","Se registró")
    }


    //Esto se ejecuta toda vez que se presente el app en la pantalla, valida si hay un usuario autenticado
    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        refresca(usuario)
    }

}