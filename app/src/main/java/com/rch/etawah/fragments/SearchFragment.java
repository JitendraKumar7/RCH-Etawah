package com.rch.etawah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rch.etawah.activity.ProductDetails;
import com.rch.etawah.Adapters.SearchAdapter;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.SearchModel;
import com.rch.etawah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.Search;

public class SearchFragment extends Fragment {

    RecyclerView recyclerSearch;
    EditText txtSearch;
    ProgressDialog progressDialog;
    SearchAdapter searchAdapter;
    List<SearchModel>searchlist=new ArrayList<>();
    String seaarchId,seaarchName;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch=view.findViewById(R.id.recyclerSearch);
        txtSearch=view.findViewById(R.id.txtSearch);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if(isOnline()){
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.toString().length() >= 2) {
                        searchlist.clear();
                        searchUrl(String.valueOf(s));
                    }
                    else if (s.toString().length() < 2) {
                        searchlist.clear();
                        if (searchAdapter != null) {
                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        recyclerSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerSearch, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                seaarchId = searchlist.get(position).getId();
                seaarchName = searchlist.get(position).getpNAme();
                Intent intent=new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("sId",seaarchId);
                intent.putExtra("sName",seaarchName);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    private void searchUrl(final String name) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Search, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Seach state..",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")){
                        searchlist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String product_name = jsonObject1.getString("product_name");
                            String cat_id= jsonObject1.getString("cat_id");
                            String product_image= jsonObject1.getString("product_image");

                            SearchModel ss=new SearchModel(product_id,product_name);
                            searchlist.add(ss);

                        }
                        searchAdapter = new SearchAdapter(searchlist);
                        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerSearch.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("keyword",name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
