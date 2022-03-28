package fr.isen.hechon.androiderestaurant

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.hechon.androiderestaurant.databinding.ActivityItemBinding
import fr.isen.hechon.androiderestaurant.domain.Item
import fr.isen.hechon.androiderestaurant.domain.LignePanier
import fr.isen.hechon.androiderestaurant.domain.Panier
import java.io.File


class ActivityItem : AppCompatActivity() {

    private lateinit var binding : ActivityItemBinding
    private lateinit var viewPager: ViewPager
    private lateinit var mViewPagerAdapter: ViewPagerAdapter
    private lateinit var panier:Panier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        this.panier=lecturePanier()

        //Get Item from previous Activity
        val item:Item= Gson().fromJson(intent.getStringExtra("Item"),Item::class.java)

        //carousel photo
        viewPager = findViewById(R.id.viewpager)
        mViewPagerAdapter = ViewPagerAdapter(this, item.images)
        viewPager.pageMargin = 15
        viewPager.setPadding(50, 0, 50, 0)
        viewPager.clipToPadding = false
        viewPager.pageMargin = 25
        viewPager.adapter = mViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        //titre produit
        binding.nomProduit.text=item.name_fr

        //texte des ingredients (concatenation des ingredients)
        binding.ingredientDetail.text=concantIng(item)

        //titre fenetre
        binding.toolbar.title = "saeddfgh"
        supportActionBar?.setDisplayShowTitleEnabled(true)
        setSupportActionBar(binding.toolbar)

        //initialisation badge sur le panier
        binding.badgePanier.text=initPanierBadge()


        //number picker et modification bouton total
        binding.numberPicker.setPickerValue(1F)//
        """Ajouter au panier - Total ${(item.prices[0].price.toFloat())} €""".also { binding.buttonTotal.text = it }
        binding.numberPicker.setClickNumberPickerListener { previousValue, currentValue, pickerClickType ->
            """Ajouter au panier - Total ${(item.prices[0].price.toFloat()*currentValue)} €""".also { binding.buttonTotal.text = it }
        }


        //clic sur bouton ajout au panier
        binding.buttonTotal.setOnClickListener {
            //snackbar
            val snack = Snackbar.make(it,"${binding.numberPicker.value.toInt()} ${item.name_fr}  ajouté au panier",Snackbar.LENGTH_LONG)
            snack.show()
            //modification texte badge et ecriture dans le fichier panier
            val commentaire:String=binding.commentaireInput.text.toString()
            binding.badgePanier.text=ecriturePanier(binding.numberPicker.value.toInt(),item, commentaire)
        }

        //clic sur le bouton panier
        binding.btnPanier.setOnClickListener {
            this.panier=lecturePanier()
            if(binding.commentaireInput.text.isNullOrBlank() || binding.commentaireInput.text.isNullOrEmpty()){
                binding.commentaireInput.setText("")
            }
            Toast.makeText(this@ActivityItem, panier.lignes.size.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ActivityPanier::class.java)
            startActivity(intent)
        }

        binding.toolbar.title=item.name_fr
        binding.toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
    }

    override fun onRestart() {
        super.onRestart()
        this.panier=lecturePanier()
        binding.badgePanier.text=panier.lignes.size.toString()
    }

    private fun concantIng(item:Item):String{
        var ingredientsString=""
        item.ingredients.forEachIndexed{index,ingredient ->
            ingredientsString += if(index==item.ingredients.size-1){
                ingredient.name_fr+"."
            }else{
                ingredient.name_fr+", "
            }
        }
        return ingredientsString
    }

    private fun initPanierBadge():String{
        //sauvegarde du panier en json dans les fichiers
        //lecture du fichier
        val filename1 = "panier.json"
        val file = File(binding.root.context.filesDir, filename1)
        //si le fichier existe on lit le panier directement dedans

        return if(file.exists()){
            val contents = file.readText()
            panier = Gson().fromJson(contents,Panier::class.java)
            panier.lignes.size.toString()
        } else{
            ""
        }
    }
    private fun lecturePanier(): Panier {
        //lecture fichier panier
        val filename = "panier.json"
        val file = File(binding.root.context.filesDir, filename)
        return if(file.exists()){
            val contents = file.readText()
            Gson().fromJson(contents, Panier::class.java)
        }else{
            Panier(ArrayList())
        }
    }
    private fun ecriturePanier(value: Int, item:Item,com:String): String {
        //sauvegarde du panier en json dans les fichiers
        val lignePanier= LignePanier(value,item,com)

        //ecriture nombre d'article dans panier
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putInt("nbItemPanier", value).apply()

        //lecture du fichier
        val filename1 = "panier.json"
        val file = File(binding.root.context.filesDir, filename1)
        //si le fichier existe on lit le panier directement dedans
        panier = if(file.exists()){
            val contents = file.readText()
            Gson().fromJson(contents,Panier::class.java)
        }
        //si le fichier n'existe pas on cree un panier vide
        else{
            Panier(ArrayList())
        }
        //puis on ajoute notre element au panier et on ecrit le fichier
        panier.lignes.add(lignePanier)
        val panierJson = Gson().toJson(panier)
        Log.d("Panier",panierJson)
        val filename = "panier.json"
        this.binding.root.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(panierJson.toByteArray())
        }
        return panier.lignes.size.toString()
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int,positionOffset: Float,positionOffsetPixels: Int) { }
            override fun onPageSelected(position: Int) {}
        }
}