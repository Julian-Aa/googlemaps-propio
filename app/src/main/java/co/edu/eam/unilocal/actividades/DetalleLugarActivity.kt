package co.edu.eam.unilocal.actividades

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.adapter.ImaganesViewPager
import co.edu.eam.unilocal.adapter.ViewPagerAdapter
import co.edu.eam.unilocal.databinding.ActivityDetalleLugarBinding
import co.edu.eam.unilocal.modelo.Lugar
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DetalleLugarActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetalleLugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val codigoLugar = intent.extras!!.getString("codigo")

        if(codigoLugar != null) {

            binding.viewPager.adapter = ViewPagerAdapter(this, codigoLugar)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.info_lugar)
                    1 -> tab.text = getString(R.string.comentarios)
                }
            }.attach()

            Firebase.firestore
                .collection("lugares")
                .document(codigoLugar)
                .get()
                .addOnSuccessListener {

                    var lugarF = it.toObject(Lugar::class.java)

                    if(lugarF != null){
                        binding.listaImgs.adapter = ImaganesViewPager(this, lugarF.imagenes)
                    }

                }



        }

    }
}