package fr.isen.hechon.androiderestaurant.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.hechon.androiderestaurant.R
import java.util.*

class BleServiceAdapter (private val bleServices:List<BLEService>,
                         private val context: Context,
                         private val readCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
                         private val writeCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
                         private val notifyCharacteristicCallback: (BluetoothGattCharacteristic, Boolean) -> Unit
) : ExpandableRecyclerViewAdapter<BleServiceAdapter.ServiceViewHolder, BleServiceAdapter.CharacteristicViewHolder>(bleServices) {

    class ServiceViewHolder(itemView: View):GroupViewHolder(itemView) {
        var serviceName: TextView = itemView.findViewById(R.id.service_name)
        var serviceUuid: TextView = itemView.findViewById(R.id.service_uuid)
        private val serviceArrow: View = itemView.findViewById(R.id.indicator)
        override fun expand() {
            serviceArrow.animate().rotation(-90f).setDuration(400L).start()
        }

        override fun collapse() {
            serviceArrow.animate().rotation(0f).setDuration(400L).start()
        }
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView){

        var characteristicName: TextView = itemView.findViewById(R.id.type_chara)
        var characteristicUuid: TextView = itemView.findViewById(R.id.uuid_chara)
        var characteristicProperties: TextView = itemView.findViewById(R.id.properties)
        var characteristicValue: TextView = itemView.findViewById(R.id.value)

        var characteristicReadAction: Button = itemView.findViewById(R.id.lire)
        var characteristicWriteAction: Button = itemView.findViewById(R.id.ecrire)
        var characteristicNotifyAction: Button = itemView.findViewById(R.id.notif)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_service, parent, false)
        )
    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): CharacteristicViewHolder =
    CharacteristicViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.layout_charac, parent, false)
    )

    override fun onBindChildViewHolder(holder: CharacteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val characteristic = group.items?.get(childIndex) as BluetoothGattCharacteristic
        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).title
        val uuidMessage = "UUID : ${characteristic.uuid}"
        holder.characteristicUuid.text = uuidMessage
        holder.characteristicName.text = title

        val properties = arrayListOf<String>()
        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Lecture",
            BluetoothGattCharacteristic.PROPERTY_READ,
            holder.characteristicReadAction,
            readCharacteristicCallback
        )
        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Ecrire",
            (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
            holder.characteristicWriteAction,
            writeCharacteristicCallback
        )
        addPropertyNotificationFromCharacteristic(
            characteristic,
            properties,
            holder.characteristicNotifyAction,
            notifyCharacteristicCallback
        )
        val proprietiesMessage = "Propriet??s : ${properties.joinToString()}"
        holder.characteristicProperties.text = proprietiesMessage

        characteristic.value?.let {
            val hex = it.joinToString("-") { byte -> "%02x".format(byte)}.uppercase(Locale.FRANCE)
            val value = "Valeur : ${String(it)} Hex : 0x$hex"
            holder.characteristicValue.visibility = View.VISIBLE
            holder.characteristicValue.text = value
        }
    }

    override fun onBindGroupViewHolder(holder: ServiceViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text=title
        holder.serviceUuid.text=group.title
    }



    ////////////////
    fun updateFromChangedCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        this.bleServices.forEach {
            val index = it.items.indexOf(characteristic)
            if(index != -1) {
                it.items.removeAt(index)
                it.items.add(index, characteristic)
            }
        }
    }
    private fun addPropertyFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyName: String,
        propertyType: Int,
        propertyAction: Button,
        propertyCallBack: (BluetoothGattCharacteristic) -> Unit
    ) {
        if ((characteristic.properties and propertyType) != 0) {
            properties.add(propertyName)
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic)
            }
        }
    }

    private fun addPropertyNotificationFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyAction: Button,
        propertyCallBack: (BluetoothGattCharacteristic, Boolean) -> Unit
    ) {
        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            properties.add("Notifier")
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            val isNotificationEnable = characteristic.descriptors.any {
                it.value?.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) ?: false
            }
            if (isNotificationEnable) {
                propertyAction.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
                propertyAction.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                propertyAction.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                propertyAction.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
            }
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic, !isNotificationEnable)
            }
        }
    }
}