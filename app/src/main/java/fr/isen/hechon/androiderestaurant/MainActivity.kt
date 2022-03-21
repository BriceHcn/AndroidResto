package fr.isen.hechon.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val btn_entry = findViewById(R.id.button_entry) as Button
        btn_entry.setOnClickListener {
            Toast.makeText(this@MainActivity, "Entrées", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CategorieActivity::class.java)
            intent.putExtra("Category","Entrees")
            startActivity(intent)
        }

        val btn_main = findViewById(R.id.button_main) as Button
        btn_main.setOnClickListener {
            Toast.makeText(this@MainActivity, "Plats", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CategorieActivity::class.java)
            intent.putExtra("Category","Plats")
            startActivity(intent)
        }

        val btn_dessert = findViewById(R.id.button_dessert) as Button
        btn_dessert.setOnClickListener {
            Toast.makeText(this@MainActivity, "Desserts", Toast.LENGTH_SHORT).show()
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