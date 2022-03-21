package fr.isen.hechon.androiderestaurant

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.hechon.androiderestaurant.databinding.ActivityCategorieBinding
import fr.isen.hechon.androiderestaurant.databinding.ActivityHomeBinding
import fr.isen.hechon.androiderestaurant.domain.ApiData
import fr.isen.hechon.androiderestaurant.domain.Item
import org.json.JSONObject
import java.nio.charset.Charset


class CategorieActivity : AppCompatActivity() {

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
        setTitle(categoryName)

        //setup du recycler view
        val recyclerView: RecyclerView = binding.recyclerView
        customAdapter = CustomAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

        //fetch from api
        getDataFromApi()
    }

    fun getDataFromApi(){
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
                    apiData.data[0]
                    Log.d("API", strResp)
                    fillRecyclerView(apiData)
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
        //TODO faire selon le parametre donné en entrées
        if(intent.getStringExtra("Category")=="Plats"){
            dataApi.data[1].items.forEach { item: Item -> itemsList.add(item) }
        }
        else if(intent.getStringExtra("Category")=="Desserts"){
            dataApi.data[2].items.forEach { item: Item -> itemsList.add(item) }
        }else{
            dataApi.data[0].items.forEach { item: Item -> itemsList.add(item) }
        }
        customAdapter.notifyDataSetChanged()
    }

}