package fr.isen.hechon.androiderestaurant.ble

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hechon.androiderestaurant.databinding.ItemBleBinding

internal class ListBleAdapter(private var itemsList: MutableList<ScanResult>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<ListBleAdapter.MyViewHolder>() {

    private lateinit var binding: ItemBleBinding

    internal inner class MyViewHolder(binding: ItemBleBinding) : RecyclerView.ViewHolder(binding.root) {
        val mac=binding.mac
        val name=binding.name
        val rssi=binding.rssi
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemBleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.mac.text =  item.device.address
        holder.name.text = if(item.device.name == null){"Unknow Name"}else{item.device.name}
        holder.rssi.text = "${item.rssi} Db"
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    class OnClickListener(val clickListener: (item: ScanResult) -> Unit) {
        fun onClick(item: ScanResult) = clickListener(item)
    }



}