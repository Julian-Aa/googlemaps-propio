package co.edu.eam.unilocal.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.databinding.ActivityPerfil2Binding
import co.edu.eam.unilocal.databinding.ActivityPerfilBinding
import co.edu.eam.unilocal.modelo.Lugar
import co.edu.eam.unilocal.modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PerfilActivity2 : AppCompatActivity() {

    lateinit var binding:ActivityPerfil2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfil2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()


        if (user != null){
            db.collection("usuarios")
                .document(user.uid)
                .get()
                .addOnSuccessListener {


                    val usuario = it.toObject(Usuario::class.java)
                    if (usuario != null) {
                        usuario.key = it.id
                        val nombreUsuario = usuario.nombre
                        val nicknameUsuario = usuario.nickname
                        val correoUsuario = user.email
                        binding.nombreUsuario.setText(nombreUsuario).toString()
                        binding.nicknameUsuario.setText(nicknameUsuario).toString()
                        binding.correoUsuario.setText(correoUsuario).toString()
                    }

                }

            Firebase.firestore
                .collection("usuarios")
                .document( FirebaseAuth.getInstance().currentUser!!.uid )
                .collection("favoritos")
                .get()
                .addOnSuccessListener {

                      binding.guardados.setText(it.size().toString()).toString()

                }

            val lugaresCollection = db.collection("lugares")
                .whereEqualTo("idCreador", user.uid)

            lugaresCollection.get()
                .addOnSuccessListener { lugaresDocuments ->

                    var totalCalificaciones = 0
                    var cantidadComentarios = 0

                    for (lugarDocument in lugaresDocuments) {
                        val lugar = lugarDocument.toObject(Lugar::class.java)

                        val comentariosCollection = lugarDocument.reference.collection("comentarios")
                        binding.aportes.setText(lugaresDocuments.size().toString()).toString()

                        comentariosCollection.get()
                            .addOnSuccessListener { comentariosDocuments ->
                                for (comentarioDocument in comentariosDocuments) {
                                    val calificacion = comentarioDocument.getDouble("calificacion") ?: 0.0

                                    totalCalificaciones += calificacion.toInt()
                                    cantidadComentarios++
                                }

                                val promedioCalificaciones = if (cantidadComentarios > 0) {
                                    totalCalificaciones.toDouble() / cantidadComentarios
                                } else {
                                    0.0
                                }

                                binding.calificacion.setText(promedioCalificaciones.toString()).toString()

                            }
                            .addOnFailureListener { exception ->
                            }
                    }
                }
                .addOnFailureListener { exception ->
                }

        }
        binding.botonEditarPerfil.setOnClickListener{
            irEditarPerfil()
        }
    }

    fun irEditarPerfil(){

        startActivity( Intent(this, EditarPerfilActivity::class.java) )
    }
}