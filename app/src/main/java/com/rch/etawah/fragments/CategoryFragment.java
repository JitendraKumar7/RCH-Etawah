package com.rch.etawah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.Adapters.HomeCategoryAdapter;
import com.rch.etawah.ModelClass.HomeCate;
import com.rch.etawah.R;
import com.rch.etawah.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.Categories;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView recyclerSubCate;
    HomeCategoryAdapter cateAdapter, subCateAdapter;
    private List<HomeCate> cateList = new ArrayList<>();
    private List<HomeCate> subcateList = new ArrayList<>();
    ProgressDialog progressDialog;
    String catId;
    Gson gson;
    private boolean isSubcat = false;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recyclerCAte);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.Category));
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        if (isOnline()) {
            categoryUrl();
        }

      /*  recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                catId = cateList.get(position).getId();
                get_subcategory(catId);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
        return view;
    }

    private void categoryUrl() {
        cateList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                Categories, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("categdrytyguioj", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.equals("1")) {
                            JSONArray array = response.getJSONArray("data");


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                HomeCate model = new HomeCate();


                                model.setDetail(object.getString("description"));
                                model.setId(object.getString("cat_id"));
                                model.setImages(object.getString("image"));
                                model.setName(object.getString("title"));

                                model.setSub_array(object.getJSONArray("subcategory"));
                                cateList.add(model);

                                cateAdapter = new HomeCategoryAdapter(cateList, getContext());
                                recyclerView.setAdapter(cateAdapter);
                                cateAdapter.notifyDataSetChanged();
                            }

//
//                            Gson gson = new Gson();
//                            Type listType = new TypeToken<List<HomeCate>>() {
//                            }.getType();
//                            cateList = gson.fromJson(response.getString("data"), listType);
//
                        }
                    } else {
                        // Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }


/*
    private void categoryUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Categories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Categories", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        cateList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String cat_id = jsonObject1.getString("cat_id");
                            String title = jsonObject1.getString("title");
                            String image = jsonObject1.getString("image");
                            String description = jsonObject1.getString("description");

                            JSONArray Subcategory = jsonObject1.getJSONArray("subcategory");
                            for (int j = 0; j < Subcategory.length(); j++) {
                                if(Subcategory!=null) {

                                JSONObject jsonObject2 = Subcategory.getJSONObject(j);
                                String sub_cat_id = jsonObject2.getString("cat_id");
                                String sub_title = jsonObject2.getString("title");
                                String sub_image = jsonObject2.getString("image");
                                String sub_description = jsonObject2.getString("description");
                              */
/*  HomeCate subcate=new HomeCate(sub_image,sub_title,sub_cat_id,sub_description);
                                subcateList.add(subcate);
*//*

                                  JSONArray Product = jsonObject2.getJSONArray("subchild");
                                  for (int k = 0; k < Product.length(); k++) {
                                      if (Subcategory!=null&&Product != null) {
                                          JSONObject jsonObject3 = Product.getJSONObject(k);
                                          String prod_cat_id = jsonObject3.getString("cat_id");
                                          String prod_title = jsonObject3.getString("title");
                                          String prod_image = jsonObject3.getString("image");
                                          String prod_description = jsonObject3.getString("description");
                                      } }
                               }}
                            HomeCate cate=new HomeCate(image,title,cat_id,description);
                            cateList.add(cate);
                        }
                        cateAdapter = new HomeCategoryAdapter(cateList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(cateAdapter);
                        cateAdapter.notifyDataSetChanged();


                    } else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("message");
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }


                   */
/* try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<HomeCate>>() {
                                }.getType();
                              //  cateList = gson.fromJson(response.getString("data"), listType);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            cateList = gson.fromJson(String.valueOf(jsonArray), listType);
                                cateAdapter = new HomeCategoryAdapter(cateList);
                                recyclerView.setAdapter(cateAdapter);
                            cateAdapter.notifyDataSetChanged();
                            }*//*



                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
*/

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

/*
    public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder> {

        private List<HomeCate> homeCateList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView prodNAme,pdetails;
            ImageView image;

            CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                prodNAme = (TextView) view.findViewById(R.id.pNAme);
                pdetails = (TextView) view.findViewById(R.id.pDetails);
                image = (ImageView) view.findViewById(R.id.Pimage);
                recyclerSubCate=view.findViewById(R.id.recyclerSubCate);
                cardView=view.findViewById(R.id.cardView);

            }
        }


        public HomeCategoryAdapter(List<HomeCate> homeCateList) {
            this.homeCateList = homeCateList;
        }

        @Override
        public HomeCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_homeshopcate, parent, false);

            return new HomeCategoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(HomeCategoryAdapter.MyViewHolder holder, int position) {
            HomeCate cc = homeCateList.get(position);
            holder.prodNAme.setText(cc.getName());
            holder.pdetails.setText(cc.getDetail());
            Picasso.get().load(cc.getImages()).into(holder.image);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    get_subcateory(cc.getId());
                }

                private void get_subcateory(String id) {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Categories, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("HomeTopSelling", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
                                    cateList.clear();
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String cat_id = jsonObject1.getString("cat_id");
                                        String title = jsonObject1.getString("title");
                                        String image = jsonObject1.getString("image");
                                        String description = jsonObject1.getString("description");

                                        JSONArray Subcategory = jsonObject1.getJSONArray("subcategory");
                                        for (int j = 0; j < Subcategory.length(); j++) {

                                            JSONObject jsonObject2 = Subcategory.getJSONObject(j);
                                            String sub_cat_id = jsonObject2.getString("cat_id");
                                            String sub_title = jsonObject2.getString("title");
                                            String sub_image = jsonObject2.getString("image");
                                            String sub_description = jsonObject2.getString("description");

                                            HomeCate subcate=new HomeCate(sub_image,sub_title,sub_cat_id,sub_description);
                                            subcateList.add(subcate);

                                            subCateAdapter = new HomeCategoryAdapter(subcateList);
                                            recyclerSubCate.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerSubCate.setAdapter(subCateAdapter);
                                            subCateAdapter.notifyDataSetChanged();

                                            JSONArray Product = jsonObject2.getJSONArray("subchild");
                                            for (int k = 0; k < Product.length(); k++) {

                                                JSONObject jsonObject3 = Product.getJSONObject(k);
                                                String prod_cat_id = jsonObject3.getString("cat_id");
                                                String prod_title = jsonObject3.getString("title");
                                                String prod_image = jsonObject3.getString("image");
                                                String prod_description = jsonObject3.getString("description");

                                                JSONArray SubProduct = jsonObject3.getJSONArray("subchild");
                                                for (int l = 0; l < SubProduct.length(); l++) {

                                                    JSONObject jsonObject4 = SubProduct.getJSONObject(l);
                                                    String subprod_cat_id = jsonObject4.getString("product_id");
                                                    String subprod_title = jsonObject4.getString("product_name");
                                                    String subprod_image = jsonObject4.getString("product_image");

                                                }
                                            }

                                        }

                                    }


                                } else {
                                    JSONObject resultObj = jsonObject.getJSONObject("results");
                                    String msg = resultObj.getString("message");
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.getCache().clear();
                    requestQueue.add(stringRequest);

                }
            });
        }

        @Override
        public int getItemCount() {
            return homeCateList.size();
        }
    }
*/

}
