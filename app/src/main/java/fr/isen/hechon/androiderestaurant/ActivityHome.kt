package fr.isen.hechon.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.hechon.androiderestaurant.ble.BleScanActivity
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
        //clic sur le logo
        binding.LogoApp.setOnClickListener {
            openBle()
        }
        //et sur le bouton ble
        binding.btnBle.setOnClickListener{
            openBle()
        }

        //boutton nous trouver
        binding.findUs.setOnClickListener {
            Toast.makeText(this@ActivityHome, "Maps", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openBle() {
        Toast.makeText(this@ActivityHome, "BLE", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BleScanActivity::class.java)
        startActivity(intent)
    }


    private fun changeActivity(cat:String){
        Toast.makeText(this@ActivityHome, cat, Toast.LENGTH_SHORT).show()
        val intent = Intent(this,ActivityCategorie::class.java)
        intent.putExtra("Category",cat)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("", "destroy")
        Toast.makeText(baseContext, "onDestroy", Toast.LENGTH_SHORT).show()
    }
}