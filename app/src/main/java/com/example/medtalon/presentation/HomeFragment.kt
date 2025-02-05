package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.medtalon.presentation.HomeViewModel.Events
import com.example.test2.R
import com.example.test2.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val dataList = listOf("Не найдено", "Грушевская", "Поликлиника 26")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.events.observe(viewLifecycleOwner, eventsListener)

        homeViewModel.selectedPatient.observe(viewLifecycleOwner){ patient->
            binding.patientTextView.text = patient
        }

        homeViewModel.selectedPolyclinic.observe(viewLifecycleOwner){ polyclinic->
            binding.polyclinicTextView.text = polyclinic
        }


        binding.getTalonButton.setOnClickListener {
            homeViewModel.getTalon()
        }

        binding.callDoctorButton.setOnClickListener {
            homeViewModel.callDoctor()
        }

        binding.searchButton.setOnClickListener {
            homeViewModel.search()
        }

        binding.paidServicesButton.setOnClickListener {
            homeViewModel.showPaidServices()
        }

        binding.medicalInstitutionsButton.setOnClickListener {
            homeViewModel.showMedicalInstitutions()
        }

        binding.doctorsButton.setOnClickListener{
            homeViewModel.showDoctors()
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

    private fun searchDataList(query: String): List<String> {
        return dataList.filter { it.contains(query, ignoreCase = true) }
    }

    private val eventsListener: (HomeViewModel.Events?) -> Unit = { event ->
        event?.let {
            when (it) {
                is Events.Login -> {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }

                Events.Talon -> {
                    val intent = Intent(requireContext(), GetTalonActivity::class.java)
                    startActivity(intent)
                }

                Events.Doctor -> {
                    val intent = Intent(requireContext(), CallDoctorActivity::class.java)
                    startActivity(intent)
                }

                Events.MedicalInstitutions -> {
                    val intent = Intent(requireContext(), MedicalInstitutionsActivity::class.java)
                    startActivity(intent)
                }

                Events.PayServices -> {
                    val intent = Intent(requireContext(), PaidServicesActivity::class.java)
                    startActivity(intent)
                }

                Events.Search -> {
                    val query = binding.searchEditText.text.toString()
                    val intent = Intent(requireContext(), MedicalInstitutionsActivity::class.java).apply {
                        putExtra("QUERY", query)
                    }
                    startActivity(intent)
                }

                Events.Profile -> TODO()
                is Events.Regions -> TODO()

                Events.Doctor->{
                    val intent = Intent(requireContext(), CallDoctorActivity::class.java)
                    startActivity(intent)
                }

                Events.Doctors -> {
                    val intent = Intent(requireContext(), DoctorsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}


