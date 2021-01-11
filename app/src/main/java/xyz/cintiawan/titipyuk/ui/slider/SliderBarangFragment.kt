package xyz.cintiawan.titipyuk.ui.slider


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.databinding.FragmentSliderBarangBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SliderBarangFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    lateinit var viewModel: SliderViewModel

    private lateinit var binding: FragmentSliderBarangBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slider_barang, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        factory.injectParam(arguments?.getString(IMAGE) as Any)
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(SliderViewModel::class.java)

        binding.viewModel = viewModel
        binding.imgBarang.setOnClickListener {
            startActivity(Intent(activity, ImageFullActivity::class.java)
                    .putExtra(ImageFullActivity.SOURCE, viewModel.image.value)
                    .putExtra(ImageFullActivity.IS_FILE, false))
        }
    }

    companion object {
        const val IMAGE = "image"

        fun newInstance(image: String?): SliderBarangFragment {
            val fragment = SliderBarangFragment()
            val args = Bundle()
            args.putString(IMAGE, image)
            fragment.arguments = args

            return fragment
        }
    }

}
