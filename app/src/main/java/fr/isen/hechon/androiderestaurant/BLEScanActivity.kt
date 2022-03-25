package fr.isen.hechon.androiderestaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.isen.hechon.androiderestaurant.databinding.ActivityBleBinding


class BLEScanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}