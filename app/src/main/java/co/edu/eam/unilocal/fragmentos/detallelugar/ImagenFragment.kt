package co.edu.eam.unilocal.fragmentos.detallelugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.eam.unilocal.R
import co.edu.eam.unilocal.databinding.FragmentImagenBinding
import com.bumptech.glide.Glide


class ImagenFragment : Fragment() {

    private var param1: String? = null
    private lateinit var binding:FragmentImagenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("url_img")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagenBinding.inflate(inflater, container, false)
        Glide.with(this)
            .load(param1)
            .into(binding.imgUrl)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ImagenFragment().apply {
                arguments = Bundle().apply {
                    putString("url_img", param1)
                }
            }
    }
}