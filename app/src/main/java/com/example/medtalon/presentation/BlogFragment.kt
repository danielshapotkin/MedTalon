package com.example.medtalon.presentation


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test2.databinding.FragmentAnalysisBinding
import com.example.test2.databinding.FragmentBlogBinding
import com.example.test2.databinding.FragmentHomeBinding

class BlogFragment : Fragment() {

    private lateinit var binding: FragmentBlogBinding
private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogBinding.inflate(layoutInflater)




        binding.snoreTextView.setOnClickListener {
            val url = "https://medsi.ru/articles/khrap-prichiny-i-opasnosti/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.eyesInfoTextView.setOnClickListener {
            val url = "https://www.laroche-posay.ru/blog/kak-ubrat-sinyaki-pod-glazami"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.allnewsButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.illsButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.childButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.healthButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.pillsButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.newsButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.nutritionButton.setOnClickListener{
            val url = "https://blog.talon.by/section/news/articles"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        return binding.root
    }
}

