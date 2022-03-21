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
            changeActivity("Entrées")
        }
        binding.buttonMain.setOnClickListener {
            changeActivity("Plats")
        }
        binding.buttonDessert.setOnClickListener {
            changeActivity("Desserts")
        }

    }
    fun changeActivity(cat:String){
        Toast.makeText(this@ActivityHome, cat, Toast.LENGTH_SHORT).show()
        val intent = Intent(this,CategorieActivity::class.java)
        intent.putExtra("Category",cat)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("", "destroy")
        Toast.makeText(baseContext, "onDestroy", Toast.LENGTH_SHORT).show()
    }
}