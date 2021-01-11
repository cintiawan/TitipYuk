package xyz.cintiawan.titipyuk.ui.preorder.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.shts.android.storiesprogressview.StoriesProgressView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.databinding.FragmentStoryPreOrderBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.ui.preorder.detail.PreOrderDetailActivity
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StoryPreOrderFragment : BaseFragment(), StoriesProgressView.StoriesListener {
    @Inject lateinit var factory: ViewModelFactory
    lateinit var viewModel: StoryPreOrderViewModel

    private lateinit var binding: FragmentStoryPreOrderBinding
    private lateinit var obj: PreOrderItem

    private val limit = 500L
    private var pressTime = 0L

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(view != null) {
            if(isVisibleToUser) binding.stories.startStories()
            else stop()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_story_pre_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        obj = arguments?.getParcelable(OBJECT)!!
        factory.injectParam(obj)
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(StoryPreOrderViewModel::class.java)
        viewModel.moveFragment.observe(this, Observer { if(it == 0) prevFragment() })

        binding.viewModel = viewModel

        // Initiate Story
        binding.stories.setStoriesCount(viewModel.obj.barang.gambars.size)
        binding.stories.setStoryDuration(8000L)
        binding.stories.setStoriesListener(this)

        binding.next.setOnClickListener { binding.stories.skip() }
        binding.prev.setOnClickListener { binding.stories.reverse() }
        binding.next.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressTime = System.currentTimeMillis()
                    binding.stories.pause()
                    false
                }
                MotionEvent.ACTION_UP -> {
                    binding.stories.resume()
                    limit < System.currentTimeMillis() - pressTime
                }
                else ->true
            }
        }
        binding.prev.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressTime = System.currentTimeMillis()
                    binding.stories.pause()
                    false
                }
                MotionEvent.ACTION_UP -> {
                    binding.stories.resume()
                    limit < System.currentTimeMillis() - pressTime
                }
                else ->true
            }
        }
        binding.btnDetail.setOnClickListener {
            startActivity(Intent(context, PreOrderDetailActivity::class.java)
                            .putExtra(PreOrderDetailActivity.PREORDER_ID, obj.id))
        }
    }

    override fun onPrev() {
        viewModel.prev()
    }

    override fun onNext() {
        viewModel.next()
    }

    override fun onComplete() {
        userVisibleHint = false
        nextFragment()
    }

//    override fun onPause() {
//        binding.stories.pause()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        binding.stories.startStories()
//        super.onResume()
//    }
//
//    override fun onStop() {
//        binding.stories.destroy()
//        super.onStop()
//    }
//
//    override fun onStart() {
//        binding.stories.startStories()
//        super.onStart()
//    }

    override fun onDestroy() {
        binding.stories.destroy()
        super.onDestroy()
    }

    private fun stop() {
        binding.stories.startStories()
        binding.stories.pause()
    }

    private fun nextFragment() {
        userVisibleHint = false
        (activity as StoryPreOrderActivity).nextFragment()
    }

    private fun prevFragment() {
        userVisibleHint = false
        (activity as StoryPreOrderActivity).prevFragment()
    }

    companion object {
        const val OBJECT = "object"

        fun newInstance(barang: PreOrderItem?): StoryPreOrderFragment {
            val fragment = StoryPreOrderFragment()
            val args = Bundle()
            args.putParcelable(OBJECT, barang)
            fragment.arguments = args

            return fragment
        }
    }

}
