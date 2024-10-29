package com.example.movies_app.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.movies_app.R
import com.example.movies_app.ui.favorite.FavoriteFragment
import com.example.movies_app.ui.movie.MovieFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Establecer el listener de selección
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_peliculas -> {
                    loadFragment(MovieFragment())
                    setIconColor(bottomNavigationView, R.id.navigation_peliculas)
                    true
                }
                R.id.navigation_favoritos -> {
                    loadFragment(FavoriteFragment())
                    setIconColor(bottomNavigationView, R.id.navigation_favoritos)
                    true
                }
                else -> false
            }
        }

        // Establecer el fragment inicial
        if (savedInstanceState == null) {
            loadFragment(MovieFragment())
            setIconColor(bottomNavigationView, R.id.navigation_peliculas)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun setIconColor(bottomNavigationView: BottomNavigationView, selectedItemId: Int) {
        // Cambiar dinámicamente los colores de los íconos
        for (i in 0 until bottomNavigationView.menu.size()) {
            val item = bottomNavigationView.menu.getItem(i)
            val drawable: Drawable? = item.icon
            if (drawable != null) {
                val color = if (item.itemId == selectedItemId) {
                    ContextCompat.getColor(this, R.color.purple_700)
                } else {
                    ContextCompat.getColor(this, R.color.bottom_nav_unselected_color)
                }
                drawable.setTint(color)
            }
        }
    }
}
