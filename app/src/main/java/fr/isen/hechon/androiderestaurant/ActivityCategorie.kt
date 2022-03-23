package fr.isen.hechon.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.hechon.androiderestaurant.databinding.ActivityCategorieBinding
import fr.isen.hechon.androiderestaurant.domain.ApiData
import fr.isen.hechon.androiderestaurant.domain.Item
import org.json.JSONObject
import java.nio.charset.Charset
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

class ActivityCategorie : AppCompatActivity() {

    private val itemsList = ArrayList<Item>()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var binding : ActivityCategorieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //titre
        val categoryName = intent.getStringExtra("Category")
        title = categoryName

        //setup du recycler view
        val recyclerView: RecyclerView = binding.recyclerView
        //setup click sur item
        customAdapter = CustomAdapter(itemsList,CustomAdapter.OnClickListener { item ->
            onListItemClick(item)
        })
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

        //fetch from api
        getDataFromApi()

        //handle refresh
        binding.swipeLayout.setOnRefreshListener(refreshListener)
    }

    private val refreshListener = OnRefreshListener {
        binding.swipeLayout.isRefreshing = false
        itemsList.clear()
        getDataFromApi()
    }

    private fun onListItemClick(item:Item) {
        Toast.makeText(this, item.name_fr, Toast.LENGTH_SHORT).show()
        val intent = Intent(this,ActivityItem::class.java)
        intent.putExtra("Item",Gson().toJson(item))
        startActivity(intent)
    }

    private fun getDataFromApi(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val json = JSONObject()
        json.put("id_shop", "1")
        json.toString()
        val requestBody = json.toString()

        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    // response
                    val strResp = response.toString()
                    val apiData=Gson().fromJson(strResp, ApiData::class.java)
                    Log.d("API", strResp)
                    fillRecyclerView(apiData)

                    binding.swipeLayout.isRefreshing=false
                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => $error")
                }
            ){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }

    fun fillRecyclerView(dataApi:ApiData){
        when {
            intent.getStringExtra("Category")=="Plats" -> {
                dataApi.data[1].items.forEach { item: Item -> itemsList.add(item) }
            }
            intent.getStringExtra("Category")=="Desserts" -> {
                dataApi.data[2].items.forEach { item: Item -> itemsList.add(item) }
            }
            else -> {
                dataApi.data[0].items.forEach { item: Item -> itemsList.add(item) }
            }
        }
        customAdapter.notifyDataSetChanged()
    }

}