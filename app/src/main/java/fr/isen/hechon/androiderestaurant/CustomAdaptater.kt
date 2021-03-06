package fr.isen.hechon.androiderestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.hechon.androiderestaurant.databinding.ItemBinding
import fr.isen.hechon.androiderestaurant.domain.Item

internal class CustomAdapter(private var itemsList: List<Item>,private val onClickListener: OnClickListener,private val context: Context) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    private lateinit var binding: ItemBinding

    internal inner class MyViewHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameText: TextView = binding.nomVar
        var priceVar : TextView = binding.priceVar
        var img : ImageView= binding.imgSrc

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.nameText.text=item.name_fr
        holder.priceVar.text= context.getString(R.string.euro,item.prices[0].price)

        if (item.images[0].isEmpty()) {
            holder.img.setImageResource(R.drawable.img)
        } else{
            Picasso.get().load(item.images[0]).into(holder.img)
        }
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }


    class OnClickListener(val clickListener: (item: Item) -> Unit) {
        fun onClick(item: Item) = clickListener(item)
    }

}