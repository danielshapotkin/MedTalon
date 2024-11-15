package com.example.medtalon.presentation


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test2.databinding.FragmentAnalysisBinding
import com.example.test2.databinding.FragmentHomeBinding
import com.example.test2.databinding.FragmentPillsBinding

class PillsFragment : Fragment() {

    private lateinit var binding: FragmentPillsBinding
private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPillsBinding.inflate(layoutInflater)

        binding.regionButton.setOnClickListener {
            homeViewModel.showRegions(requireView())
        }

        binding.profileButton.setOnClickListener {
            homeViewModel.showProfile()
        }

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


        return binding.root
    }
}

