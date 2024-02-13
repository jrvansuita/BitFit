package com.vansuita.bitfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.vansuita.bitfit.R
import com.vansuita.bitfit.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

	private val binding by lazy {
		ActivityMainBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?

		val appBarConfiguration = AppBarConfiguration(
			setOf(R.id.navigation_home, R.id.navigation_dashboard)
		)

		navHostFragment?.navController?.let {
			setupActionBarWithNavController(it, appBarConfiguration)
			binding.navView.setupWithNavController(it)
		}
	}

}