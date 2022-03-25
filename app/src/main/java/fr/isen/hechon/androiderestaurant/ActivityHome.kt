package fr.isen.hechon.androiderestaurant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            Toast.makeText(this@ActivityHome, "BLE", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,BLEScanActivity::class.java)
            startActivity(intent)
        }

        //boutton nous trouver
        binding.findUs.setOnClickListener {
            Toast.makeText(this@ActivityHome, "Maps", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        //permission granting
        getPermission()
    }
    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@ActivityHome,arrayOf(Manifest.permission.SET_WALLPAPER),1)
        }
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