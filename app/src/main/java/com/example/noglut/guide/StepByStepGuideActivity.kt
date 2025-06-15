package com.example.noglut.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noglut.MainActivity
import com.example.noglut.R
import com.example.noglut.auth.LoginActivity
import com.example.noglut.auth.WelcomeActivity
import com.example.noglut.databinding.ActivityStepByStepGuideBinding

class StepByStepGuideActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityStepByStepGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStepByStepGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val guideItems = listOf(
            GuideViewModel(R.drawable.step1_rest_image, getString(R.string.all_gluten_free), getString(R.string.find_the_best_restaurants)),
            GuideViewModel(R.drawable.step2_plate_image, getString(R.string.make_it_easy), getString(R.string.find_restaurants_filtered_to_your_specifications)),
            GuideViewModel(R.drawable.step3_map_image, getString(R.string.enjoy_delicious_places), getString(R.string.find_useful_reviews_save_share_your_discoveries_with_others))
        )
        val adapter = GuideAdapter(guideItems, object : GuideAdapter.OnboardingNavigationListener {
            override fun onNextClicked(position: Int) {
                if (position < guideItems.size - 1) {
                    binding.guideViewPager.currentItem = position + 1
                } else {
                    // Navigate to Welcome Activity
                    val intent = Intent(this@StepByStepGuideActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
        binding.guideViewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.guideViewPager)
    }
}

class GuideAdapter(private val items: List<GuideViewModel>, private val listener: OnboardingNavigationListener) :
    RecyclerView.Adapter<GuideAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image_view_guide)
        private val title = view.findViewById<TextView>(R.id.text_view_title)
        private val desc = view.findViewById<TextView>(R.id.text_view_description)
        val skip = view.findViewById<TextView>(R.id.btn_skip)
        val next = view.findViewById<TextView>(R.id.btn_next)

        fun bind(item: GuideViewModel) {
            image.setImageResource(item.imageRes)
            title.text = item.title
            desc.text = item.description
        }

    }

    interface OnboardingNavigationListener {
        fun onNextClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.guide_item, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        if (position == 2) {
            holder.skip.visibility = INVISIBLE
            holder.next.setText(R.string.get_started_button)
        } else {
            holder.skip.visibility = VISIBLE
            holder.next.setText(R.string.next_button)
        }
        holder.next.setOnClickListener {
            listener.onNextClicked(position)
        }
        holder.skip.setOnClickListener {
            listener.onNextClicked(position)
        }
        holder.bind(items[position])
    }
}
