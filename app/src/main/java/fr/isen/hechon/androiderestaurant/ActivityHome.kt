package fr.isen.hechon.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import fr.isen.hechon.androiderestaurant.databinding.ActivityHomeBinding

class ActivityHome : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonEntry.setOnClickListener {
            Toast.makeText(this@ActivityHome, "Entrées", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CategorieActivity::class.java)
            intent.putExtra("Category","Entrees")
            startActivity(intent)
        }


        binding.buttonMain.setOnClickListener {
            Toast.makeText(this@ActivityHome, "Plats", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CategorieActivity::class.java)
            intent.putExtra("Category","Plats")
            startActivity(intent)
        }

        binding.buttonDessert.setOnClickListener {
            Toast.makeText(this@ActivityHome, "Desserts", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CategorieActivity::class.java)
            intent.putExtra("Category","Desserts")
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("", "destroy")
        Toast.makeText(baseContext, "onDestroy", Toast.LENGTH_SHORT).show()
    }
}