package fr.isen.hechon.androiderestaurant.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.hechon.androiderestaurant.databinding.ActivityBledeviceBinding


class BLEDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBledeviceBinding
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        val result :ScanResult = intent.getParcelableExtra("Device")!!

        binding.deviceName.text=result.device.name

        Toast.makeText(this, result.device.address, Toast.LENGTH_SHORT).show()
        val gatt:BluetoothGatt=result.device.connectGatt(this, true, gattCallback)
        gatt.discoverServices()


        Log.d("SALUT","Salut1")
    }


    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            Log.d("SALUT","Salut2")
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.d("SALUT","Salut3")
                //gatt?.requestMtu(256)
                gatt?.discoverServices()
                runOnUiThread {  binding.deviceStatus.text = "Connecté"}
            }else if(newState == BluetoothGatt.STATE_CONNECTING){
                runOnUiThread {  binding.deviceStatus.text = "Connexion en cours"}
            }else{runOnUiThread {  binding.deviceStatus.text = "Pas connecté"}
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            Log.d("BluetoothLeService", "onServicesDiscovered()")
            val gattServices: List<BluetoothGattService> = gatt!!.services
            Log.d("onServicesDiscovered", "Services count: " + gattServices.size)
            for (gattService in gattServices) {
                val serviceUUID = gattService.uuid.toString()
                Log.d("onServicesDiscovered", "Service uuid $serviceUUID")

                val gattChara: List<BluetoothGattCharacteristic> = gattService.characteristics
                for(gattC in gattChara ){
                    val charaUUID = gattC.uuid.toString()
                    Log.d("onServicesDiscovered", "        cara uuid $charaUUID")
                }

            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) { }

    }
}

