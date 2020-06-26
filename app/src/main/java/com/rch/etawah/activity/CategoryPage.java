package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rch.etawah.Adapters.CategoryGridAdapter;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.fragments.Recent_Details_Fragment;
import com.rch.etawah.ModelClass.CategoryGrid;
import com.rch.etawah.ModelClass.varient_product;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryPage extends AppCompatActivity {

    RecyclerView recycler_product ;
    CategoryGridAdapter  adapter ;
    List<CategoryGrid>  model= new ArrayList<>();
    String cat_id , image,title;
    private List<varient_product> varientProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        recycler_product = findViewById( R.id.recycler_product);
        image = Recent_Details_Fragment.product_image;


        cat_id = getIntent().getStringExtra("cat_id");
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");




        recycler_product.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CategoryGridAdapter(model,CategoryPage.this);
        recycler_product.setAdapter(adapter);


        product(cat_id);



    }


    private void product(String cat_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.cat_product, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());


                try {
                    String status = response.getString("status");

                    String message = response.getString("message");

                    if (status.contains("1")){

                    JSONArray data = response.getJSONArray("data");
                    for (int i =0 ; i<data.length();i++) {

                        JSONObject object = data.getJSONObject(i);

                        CategoryGrid models = new CategoryGrid();

                        models.setId(object.getString("product_id"));

                        models.setImage(object.getString("product_image"));
                        models.setName(object.getString("product_name"));


                        JSONArray dataobj2 = object.getJSONArray("varients");

                        String qty= "";
                        String unit= "";
                        String description ="";
                        String mrp= "";
                        String price= "";
                        String varient_id = "";
                        String varient_image= "";
                        if (dataobj2.length()>0) {
                            JSONObject jsonObject = dataobj2.getJSONObject(0);

                            qty = jsonObject.getString("quantity");
                            unit = jsonObject.getString("unit");
                            mrp = jsonObject.getString("mrp");
                            description = jsonObject.getString("description");

                            varient_id = jsonObject.getString("varient_id");
                            price = jsonObject.getString("price");
                            varient_image = jsonObject.getString("varient_image");
                            models.setVarient_id(varient_id);
                            models.setVarient_image(varient_image);
                            models.setPrice(price);
                            models.setMrp(mrp);
                            models.setUnit(unit);
                            models.setQuantity(qty);
                            models.setDescription(description);
                            model.add(models);

                            adapter.notifyDataSetChanged();
                        }

//                        adapter.notify();
                    }


                    }else {

//                        Toast.makeText(CategoryPage.this, "", Toast.LENGTH_SHORT).show();
                    }






                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }




}
