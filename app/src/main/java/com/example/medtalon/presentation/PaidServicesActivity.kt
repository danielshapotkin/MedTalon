package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.medtalon.adapters.PaidServicesAdapter
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.PaidService
import com.example.test2.R
import com.example.test2.databinding.ActivityPaidServicesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PaidServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaidServicesBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val dataBase: DataBase = DataBase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaidServicesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            view.findViewById<Button>(R.id.view_profile_button).setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.settings_button).setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
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

        dataBase.getPaidServices { success, paidServices, error ->
            if (success && paidServices != null) {
                updateListView(paidServices)
            } else {
                println("Ошибка при получении поликлиник: $error")
            }
        }

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.indicator)
        viewPager.adapter = ViewPagerAdapter(this)

        // Link the TabLayout and the ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        // Список данных для страниц
        private val pages = listOf(
            Pair("Удобно", "Выбирайте ближайший к вам медцентр, удобный день и время приема"),
            Pair("Актуальная цена", "Реальные цены медицинских учреждений"),
            Pair("Без забот", "Храните историю посещений в своемм личном кабинете на сайте")
        )

        override fun getItemCount(): Int = pages.size // Количество страниц

        override fun createFragment(position: Int): Fragment {
            val (title, text) = pages[position]
            return PageFragment.newInstance(title, text)
        }
    }

    class PageFragment : Fragment(R.layout.fragment_page) {

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Получаем данные из аргументов
            val title = arguments?.getString("title") ?: "Default Title"
            val text = arguments?.getString("text") ?: "Default Text"

            // Устанавливаем данные в TextView
            view.findViewById<TextView>(R.id.page_title).text = title
            view.findViewById<TextView>(R.id.page_text).text = text
        }

        companion object {
            // Метод для создания фрагмента с данными
            fun newInstance(title: String, text: String): PageFragment {
                val fragment = PageFragment()
                val args = Bundle()
                args.putString("title", title)
                args.putString("text", text)
                fragment.arguments = args
                return fragment
            }
        }
    }


    private fun updateListView(paidServices: List<PaidService>) {
        val adapter = PaidServicesAdapter(this, paidServices)
        binding.paidServicesListView.adapter = adapter
    }
}
