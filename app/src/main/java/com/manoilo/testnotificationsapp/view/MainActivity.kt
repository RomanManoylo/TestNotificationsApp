package com.manoilo.testnotificationsapp.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.manoilo.testnotificationsapp.INTENT_PAGE_NUMBER
import com.manoilo.testnotificationsapp.databinding.ActivityMainBinding
import com.manoilo.testnotificationsapp.factory.ViewModelFactory
import com.manoilo.testnotificationsapp.viewmodel.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private var adapter: ScreenSlidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel(intent.getIntExtra(INTENT_PAGE_NUMBER, 0) - 1)

        initViewPager()

        binding.addPage.setOnClickListener {
            viewModel.addPage()
        }

        binding.deletePage.setOnClickListener {
            viewModel.deletePage()
            NotificationManagerCompat.from(this).cancel(viewModel.pagesCount.value!! + 1)
        }

    }

    private fun initViewPager() {
        adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        for (i in 1..viewModel.pagesCount.value!!)
            adapter?.addFragment()

        binding.viewPager.adapter = adapter
        val viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentPage(position)
                }
            }
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
    }

    private fun initViewModel(currentPage: Int) {
        val newViewModel by viewModels<MainViewModel> {
            ViewModelFactory(this.getPreferences(Context.MODE_PRIVATE), currentPage)
        }
        val pagesCountObserver = Observer<Int> {
            if (it > adapter!!.count)
                adapter?.addFragment()
            else if (it < adapter!!.count) adapter?.removeFragment(it)

            binding.deletePage.isVisible = it != 1
        }
        val currentPageNumberObserver = Observer<Int> {
            binding.pageNumber.text = (it + 1).toString()
            binding.viewPager.currentItem = it
        }
        viewModel = newViewModel
        viewModel.pagesCount.observe(this, pagesCountObserver)
        viewModel.currentPageNumber.observe(this, currentPageNumberObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

        val fragmentList: MutableList<Fragment> = ArrayList()

        override fun getCount(): Int = fragmentList.size

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment() {
            fragmentList.add(
                NotificationFragment.newInstance(count + 1)
            )
            notifyDataSetChanged()
        }

        fun removeFragment(position: Int) {
            fragmentList.removeAt(position)
            notifyDataSetChanged()
        }
    }


}