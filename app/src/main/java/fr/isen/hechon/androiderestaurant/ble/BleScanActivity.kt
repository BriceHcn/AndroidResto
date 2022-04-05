package fr.isen.hechon.androiderestaurant.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.hechon.androiderestaurant.R
import fr.isen.hechon.androiderestaurant.databinding.ActivityBleBinding

class BleScanActivity : AppCompatActivity() {
    companion object{
        private const val ALL_PERMISSION_REQUEST_CODE = 1
        private const val DEVICE_KEY="Device"
        private const val MAX_RSSI = 90
        private const val SCAN_DELAY = 15000
    }
    private var scanning:Boolean=false
    private lateinit var binding : ActivityBleBinding
    private val bluetoothAdapter: BluetoothAdapter ? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var itemsList = ArrayList<ScanResult>()

    private lateinit var listBleAdapter: ListBleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        when {
            bluetoothAdapter?.isEnabled == true ->
            binding.imgPlayBle.setOnClickListener {
                startLeScanBLEWithPermission(!scanning)
            }
            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
                displayNoBleAvailable()
            }
        }


        val recyclerBle: RecyclerView = binding.ListBleScan
        listBleAdapter = ListBleAdapter(itemsList, ListBleAdapter.OnClickListener { item ->
            onListBleClick(item)
        },
        this@BleScanActivity)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerBle.layoutManager = layoutManager
        recyclerBle.adapter = listBleAdapter

    }

    @SuppressLint("MissingPermission")
    private fun onListBleClick(item: ScanResult) {
        if(item.device.name.isNullOrEmpty()){
            Toast.makeText(this@BleScanActivity, getString(R.string.unknown_name), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@BleScanActivity, item.device.name.toString(), Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(this, BLEDeviceActivity::class.java)
        intent.putExtra(DEVICE_KEY, item.device)
        startActivity(intent)
    }

    override fun onStop(){
        super.onStop()
        startLeScanBLEWithPermission(false)
    }

    private fun startLeScanBLEWithPermission(enable: Boolean){
        if (checkAllPermissionGranted()) {
            startLeScanBLE(enable)
        }else{
            ActivityCompat.requestPermissions(this, getAllPermissions() ,ALL_PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkAllPermissionGranted(): Boolean {
        return getAllPermissions().all { permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable: Boolean) {

        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if(enable) {
                val handler = Handler(mainLooper)
                handler.postDelayed({
                    scanning=false// stop scanning here
                    binding.imgPlayBle.setImageResource(R.drawable.play)
                    binding.textScan.text = getString(R.string.lancer_scan)
                    binding.progressBarScanBle.isIndeterminate = false
                    stopScan(scanCallBack)
                }, SCAN_DELAY.toLong())
                scanning=true
                startScan(scanCallBack)
            }else{
                scanning=false
                stopScan(scanCallBack)
            }
            btnPlayClick()
        }
    }

    private val scanCallBack = object : ScanCallback(){
    override fun onScanResult(callBackType : Int, result:ScanResult){
        Log.d("BLEScanActivity","${result.device.address} - ${result.rssi}")
        addToList(result)

    }
}

    private fun addToList(res:ScanResult){
        val index:Int = itemsList.indexOfFirst{ it.device.address==res.device.address }
        if(index == -1){
            if(kotlin.math.abs(res.rssi)< MAX_RSSI) {
                itemsList.add(res)
            }
        }else{
            if(kotlin.math.abs(res.rssi)> MAX_RSSI){
                itemsList.remove(res)
            }else {
                itemsList[index] = res
            }
        }

        itemsList.sortBy { kotlin.math.abs(it.rssi) }
        listBleAdapter.notifyDataSetChanged()
    }

    private fun displayNoBleAvailable() {
        binding.imgPlayBle.isVisible=false
        binding.textScan.text=getString(R.string.lancer_scan)
        binding.progressBarScanBle.isIndeterminate=true
    }

    private fun btnPlayClick() {
            if (scanning) {
                binding.imgPlayBle.setImageResource(R.drawable.pause)
                binding.textScan.text = getString(R.string.scan_loading)
                binding.progressBarScanBle.isIndeterminate = true
                listBleAdapter.clearResults()
                listBleAdapter.notifyDataSetChanged()
            } else {
                binding.imgPlayBle.setImageResource(R.drawable.play)
                binding.textScan.text = getString(R.string.lancer_scan)
                binding.progressBarScanBle.isIndeterminate = false
            }
    }

    private fun askBluetoothPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(enableBtIntent, ALL_PERMISSION_REQUEST_CODE)
        }
    }
}