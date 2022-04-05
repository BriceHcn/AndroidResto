package fr.isen.hechon.androiderestaurant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.isen.hechon.androiderestaurant.databinding.ActivityPanierBinding
import fr.isen.hechon.androiderestaurant.domain.LignePanier
import fr.isen.hechon.androiderestaurant.domain.Panier
import java.io.File


class ActivityPanier : AppCompatActivity() {
    private lateinit var binding : ActivityPanierBinding
    private val itemsList = ArrayList<LignePanier>()
    private lateinit var panierAdapter: PanierAdapter
    private lateinit var panier:Panier


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanierBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //recuperation panier depuis fichier
        panier = lecturePanier()

        //titre fenetre
        title = "Panier"

        //setup du recycler view
        val recyclerPanier: RecyclerView = binding.recyclerPanier
        panierAdapter = PanierAdapter(itemsList, PanierAdapter.OnClickListener { item ->
            onListPanierClickDelete(item)
        },this@ActivityPanier)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerPanier.layoutManager = layoutManager
        recyclerPanier.adapter = panierAdapter
        panier.lignes.forEach { ligne: LignePanier -> itemsList.add(ligne) }
        panierAdapter.notifyDataSetChanged()

        //prix
        updateTotalPrice()

        //boutton passer commande
        binding.button.setOnClickListener {

            //startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:0641973974")))
            var cmd ="Bravo Brice 20/20 - Le prof d'android. Voici le contenu de ma commande :\n"
            panier.lignes.forEach { ligne:LignePanier -> cmd+="${ligne.Item.name_fr} * ${ligne.quantite} (${ligne.com})\n" }
            //sendSMS("+33641973974", cmd)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/brice-hechon/")))


        }
    }

    private fun updateTotalPrice(){
        var grandTotalPrice = 0.0F
        this.panier.lignes.forEach { ligne:LignePanier -> grandTotalPrice+=ligne.Item.prices[0].price.toFloat()*ligne.quantite }
        binding.GrandTotalPrice.text=getString(R.string.euro,grandTotalPrice.toString())
    }

    private fun onListPanierClickDelete(item: LignePanier) {
        Toast.makeText(this@ActivityPanier, "${item.quantite} ${item.Item.name_fr} enlevé(s) du panier",Toast.LENGTH_SHORT).show()
        this.itemsList.remove(item)
        this.panier.lignes.remove(item)
        Log.d("PANIER",panier.lignes.size.toString())
        this.ecriturePanier()
        this.updateTotalPrice()
        this.panierAdapter.notifyDataSetChanged()
    }

    private fun ecriturePanier(){
        //sauvegarde du panier en json dans les fichiers
        val panierJson = Gson().toJson(panier)
        val filename = "panier.json"
        this.binding.root.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(panierJson.toByteArray())
        }
    }

    private fun lecturePanier(): Panier {
        //lecture fichier panier
        val filename = "panier.json"
        val file = File(binding.root.context.filesDir, filename)
        if(file.exists()){
            val contents = file.readText()
            return Gson().fromJson(contents, Panier::class.java)
        }else{
            return Panier(ArrayList())
        }
    }
}