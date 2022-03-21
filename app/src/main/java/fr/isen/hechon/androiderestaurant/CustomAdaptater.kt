package fr.isen.hechon.androiderestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hechon.androiderestaurant.domain.Item

internal class CustomAdapter(private var itemsList: List<Item>) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.findViewById(R.id.nom_var)
        var ingText: TextView = view.findViewById(R.id.ing_var)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.nameText.text=item.name_fr
        holder.ingText.text= item.ingredients[0].name_fr
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
}