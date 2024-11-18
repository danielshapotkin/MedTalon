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

                is Events.Regions -> {
                    val popupMenu = PopupMenu(requireContext(), binding.regionButton)
                    popupMenu.menuInflater.inflate(R.menu.region_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        binding.regionButton.text = item.title
                        true
                    }
                    popupMenu.show()
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
                    val intent = Intent(requireContext(), MedicalInstitutionsActivity::class.java)
                    startActivity(intent)
                }

                Events.Search -> {
                    val query = binding.searchEditText.text.toString()
                    val intent = Intent(requireContext(), SearchResultActivity::class.java).apply {
                        putExtra("DOCTOR_NAME", query)
                    }
                    startActivity(intent)
                }

                Events.Profile -> {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
                    view.findViewById<Button>(R.id.view_profile_button).setOnClickListener {
                        val intent = Intent(requireContext(), ProfileActivity::class.java)
                        startActivity(intent)
                        bottomSheetDialog.dismiss()
                    }

                    view.findViewById<Button>(R.id.settings_button).setOnClickListener {
                        val intent = Intent(requireContext(), SettingsActivity::class.java)
                        startActivity(intent)
                        bottomSheetDialog.dismiss()
                    }

                    view.findViewById<Button>(R.id.logout_button).setOnClickListener {
                        homeViewModel.logout()
                        bottomSheetDialog.dismiss()
                    }

                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.show()
                }
            }
        }
    }


}


