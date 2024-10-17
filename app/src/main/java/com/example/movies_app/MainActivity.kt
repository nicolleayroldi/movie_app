package com.example.movies_app

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movies_app.model.Movie
import com.example.movies_app.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Establecer el listener de selección
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_peliculas -> {
                    loadFragment(MoviesFragment())
                    setIconColor(bottomNavigationView, R.id.navigation_peliculas)
                    true
                }
                R.id.navigation_favoritos -> {
                    loadFragment(FavoritesFragment())
                    setIconColor(bottomNavigationView, R.id.navigation_favoritos)
                    true
                }
                else -> false
            }
        }

        // Establecer el fragment inicial
        if (savedInstanceState == null) {
            loadFragment(MoviesFragment())
            setIconColor(bottomNavigationView, R.id.navigation_peliculas)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun getShowDetails(showId: Int) {
        val apiService = RetrofitClient.instance

        apiService.getShowDetails(showId).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val showDetails = response.body()
                    // Mostrar detalles del show
                    Toast.makeText(this@MainActivity, "Show: ${showDetails?.name}", Toast.LENGTH_LONG).show()
                } else {
                    // Manejar error
                    val errorMessage = "Error ${response.code()}: ${response.message()}"
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                // Manejar error de red
                Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setIconColor(bottomNavigationView: BottomNavigationView, selectedItemId: Int) {
        for (i in 0 until bottomNavigationView.menu.size()) {
            val item = bottomNavigationView.menu.getItem(i)
            val drawable: Drawable? = item.icon
            if (drawable != null) {
                // Cambia el color del icono seleccionado
                val color = if (item.itemId == selectedItemId) {
                    R.color.purple_700 // Color del icono seleccionado
                } else {
                    R.color.bottom_nav_unselected_color // Color del icono no seleccionado
                }
                drawable.setTint(resources.getColor(color, null))
            }
        }
    }
}
