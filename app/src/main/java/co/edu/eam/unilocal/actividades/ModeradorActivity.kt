package co.edu.eam.unilocal.actividades

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.unilocal.adapter.LugarAdapter
import co.edu.eam.unilocal.bd.Lugares
import co.edu.eam.unilocal.databinding.ActivityModeradorBinding
import co.edu.eam.unilocal.modelo.EstadoLugar
import co.edu.eam.unilocal.modelo.Lugar
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.adapter.ListasAdapter
import co.edu.eam.unilocal.utils.Idioma
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class ModeradorActivity : AppCompatActivity() {

    lateinit var binding:ActivityModeradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, getStatusBarHeight(), 0, 0)
        binding.tabs.layoutParams = params


        var user = FirebaseAuth.getInstance().currentUser

        if(user!=null) {

            binding.viewPager.adapter = ListasAdapter(this)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = EstadoLugar.SIN_REVISAR.name
                    1 -> tab.text = EstadoLugar.RECHAZADO.name
                    2 -> tab.text = EstadoLugar.ACEPTADO.name
                }
            }.attach()

        }
        binding.btnMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }

    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.barra_navegacion_moderador, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.opcion1 -> {
                    cambiarIdioma()
                    true
                }
                R.id.opcion2 -> {
                    confirmLogout()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    fun cambiarIdioma(){
        Idioma.selecionarIdioma(this)

        val intent = intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }

    }


    private fun confirmLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Cierre de Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        builder.setPositiveButton("Sí") { dialog, which ->
            cerrarSesion()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()
    }


}