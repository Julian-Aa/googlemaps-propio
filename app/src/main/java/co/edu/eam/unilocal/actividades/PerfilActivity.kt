package co.edu.eam.unilocal.actividades

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.adapter.LugarAdapter
import co.edu.eam.unilocal.databinding.ActivityLoginBinding
import co.edu.eam.unilocal.databinding.ActivityPerfilBinding
import co.edu.eam.unilocal.modelo.EstadoLugar
import co.edu.eam.unilocal.modelo.Lugar
import co.edu.eam.unilocal.modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    lateinit var binding:ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()


        if (user != null){
            db.collection("usuarios")
                .document(user.uid)
                .get()
                .addOnSuccessListener { l ->


                    val usuario = l.toObject(Usuario::class.java)
                    if (usuario != null) {
                        usuario.key = l.id
                        val nombreUsuario = usuario.nombre
                        val nicknameUsuario = usuario.nickname
                        val correoUsuario = user.email
                        binding.nombreUsuario.setText(nombreUsuario).toString()
                        binding.nicknameUsuario.setText(nicknameUsuario).toString()
                        binding.correoUsuario.setText(correoUsuario).toString()
                    }

                }

                }
        }








    }
