package fr.isen.hechon.androiderestaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hechon.androiderestaurant.domain.LignePanier
import fr.isen.hechon.androiderestaurant.databinding.ItemPanierBinding


internal class PanierAdapter(private var itemsList: List<LignePanier>) : RecyclerView.Adapter<PanierAdapter.MyViewHolder>() {

    private lateinit var binding: ItemPanierBinding

    internal inner class MyViewHolder(binding: ItemPanierBinding) : RecyclerView.ViewHolder(binding.root) {
        val name=binding.nameItem
        val quantity=binding.quantite
        val totalItemPrice=binding.itemTotalPrice
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemPanierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.name.text=item.Item.name_fr
        holder.quantity.text=item.quantite.toString()
        holder.totalItemPrice.text=(item.Item.prices[0].price.toInt()*item.quantite).toString()+" €"
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }




}