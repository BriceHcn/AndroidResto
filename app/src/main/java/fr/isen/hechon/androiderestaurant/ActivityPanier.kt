package fr.isen.hechon.androiderestaurant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanierBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val panier = lecturePanier()

        title="Panier"

        //setup du recycler view
        val recyclerPanier: RecyclerView = binding.recyclerPanier
        panierAdapter = PanierAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerPanier.layoutManager = layoutManager
        recyclerPanier.adapter = panierAdapter

        panier.lignes.forEach { ligne: LignePanier-> itemsList.add(ligne) }
        panierAdapter.notifyDataSetChanged()
        var grandTotalPrice = 0
            panier.lignes.forEach { ligne:LignePanier -> grandTotalPrice+=ligne.Item.prices[0].price.toInt()*ligne.quantite }
        binding.GrandTotalPrice.text=grandTotalPrice.toString()+" €"

        binding.button.setOnClickListener {
            val number = "0641973974"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
        }
    }

    //todo grouper les items du meme type dans le panier
    //todo permettre de supprimer des items dans le panier
    //todo passer la commande
    //todo btn panier dans la liste de produits aussi

    private fun lecturePanier(): Panier {
        //lecture fichier panier
        val filename = "panier.json"
        val file = File(binding.root.context.filesDir, filename)
        val contents = file.readText()
        return Gson().fromJson(contents, Panier::class.java)
    }
}