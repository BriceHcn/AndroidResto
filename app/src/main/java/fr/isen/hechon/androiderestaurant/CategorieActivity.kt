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
import fr.isen.hechon.androiderestaurant.domain.ApiData
import fr.isen.hechon.androiderestaurant.domain.Item
import org.json.JSONObject
import java.nio.charset.Charset


class CategorieActivity : AppCompatActivity() {

    private val itemsList = ArrayList<Item>()
    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorie)

        val categoryName = intent.getStringExtra("Category")

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        customAdapter = CustomAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

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
        Log.d("APIObject", dataApi.data[0].items[0].name_fr)
        dataApi.data[0].items.forEach { item: Item -> itemsList.add(item) }
        customAdapter.notifyDataSetChanged()
    }

}