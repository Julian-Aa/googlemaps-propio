package co.edu.eam.unilocal.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.adapter.LugarAdapter
import co.edu.eam.unilocal.bd.Lugares
import co.edu.eam.unilocal.databinding.ActivityResultadoBusquedaBinding
import co.edu.eam.unilocal.databinding.FragmentImagenBinding
import co.edu.eam.unilocal.fragmentos.MenuPrincipalFragment
import co.edu.eam.unilocal.modelo.EstadoLugar
import co.edu.eam.unilocal.modelo.Lugar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class ResultadoBusquedaActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultadoBusquedaBinding
    var textoBusqueda: String = ""
    var info: String = ""
    lateinit var listaLugares: ArrayList<Lugar>
    lateinit var adapterLista:LugarAdapter
    lateinit var bindingSearch: MenuPrincipalFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        info = intent.extras!!.getString("texto").toString()
        println(info)
        textoBusqueda = intent.extras!!.getString("texto", "")
        listaLugares = ArrayList()


        adapterLista = LugarAdapter(listaLugares)
        binding.listaLugares.adapter = adapterLista
        binding.listaLugares.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val db = FirebaseFirestore.getInstance()

        db.collection("lugares")
            .whereEqualTo("estado", EstadoLugar.ACEPTADO)
            .whereGreaterThanOrEqualTo("nombre", textoBusqueda)
            .whereLessThanOrEqualTo("nombre", textoBusqueda + "\uf8ff")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val lugar = document.toObject(Lugar::class.java)
                    lugar.key = document.id
                    listaLugares.add(lugar)
                }

                adapterLista.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                val adapter = LugarAdapter(listaLugares)
                binding.listaLugares.adapter = adapter
                binding.listaLugares.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            }




    }
}