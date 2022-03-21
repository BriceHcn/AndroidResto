package fr.isen.hechon.androiderestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.hechon.androiderestaurant.databinding.ItemBinding
import fr.isen.hechon.androiderestaurant.domain.Item

internal class CustomAdapter(private var itemsList: List<Item>) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    private lateinit var binding: ItemBinding

    internal inner class MyViewHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameText: TextView = binding.nomVar
        var ingText: TextView = binding.ingVar
        //var img : ImageView= binding.itemImg

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.nameText.text=item.name_fr
        holder.ingText.text= item.ingredients[0].name_fr

        /*if (item.images[0].isEmpty()) {
            holder.img.setImageResource(R.drawable.img)
        } else{
            Picasso.get().load(item.images[0]).into(holder.img)
        }*/
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
}