package co.edu.eam.unilocal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.edu.eam.unilocal.fragmentos.detallelugar.ImagenFragment

class ImaganesViewPager(var fragment: FragmentActivity, private var imagenes:ArrayList<String>): FragmentStateAdapter(fragment) {

    override fun getItemCount() = imagenes.size

    override fun createFragment(position: Int): Fragment {

        when (position) {
         position -> return ImagenFragment.newInstance(imagenes[position])
        }
       return Fragment()
    }
}