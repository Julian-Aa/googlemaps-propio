package co.edu.eam.unilocal.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.databinding.ActivityEditarPerfilBinding
import co.edu.eam.unilocal.databinding.ActivityPerfil2Binding
import co.edu.eam.unilocal.modelo.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarPerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditarPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()


        if (user != null) {
            db.collection("usuarios")
                .document(user.uid)
                .get()
                .addOnSuccessListener {


                    val usuario = it.toObject(Usuario::class.java)
                    if (usuario != null) {
                        usuario.key = it.id
                        val nombreUsuario = usuario.nombre
                        val nicknameUsuario = usuario.nickname
                        binding.editTextName.setText(nombreUsuario).toString()
                        binding.editTextNickname.setText(nicknameUsuario).toString()
                    }

                }
        }

        binding.botonActualizarPerfil.setOnClickListener {
            confirmChanges()
        }
    }

    private fun confirmChanges() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.confirmar_cambios.toString())
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { dialog, which ->
            actualizarPerfil()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun actualizarPerfil(){

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val nuevoNombre = binding.editTextName.text.toString()
            val nuevoNickname = binding.editTextNickname.text.toString()

            Firebase.firestore.collection("usuarios")
                .whereEqualTo("nickname", nuevoNickname)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        val usuarioRef = Firebase.firestore.collection("usuarios").document(user.uid)
                        usuarioRef.update("nombre", nuevoNombre, "nickname", nuevoNickname)
                            .addOnSuccessListener {
                                Snackbar.make(binding.root, "Perfil actualizado", Snackbar.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {
                                Snackbar.make(binding.root, "Error al actualizar el perfil: ${it.message}", Snackbar.LENGTH_LONG).show()
                            }
                    } else {
                        Snackbar.make(binding.root, "El nuevo nickname ya está en uso", Snackbar.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    // Manejar la falla al verificar el nickname
                    Snackbar.make(binding.root, "Error al verificar el nuevo nickname: ${it.message}", Snackbar.LENGTH_LONG).show()
                }
        }


    }
}