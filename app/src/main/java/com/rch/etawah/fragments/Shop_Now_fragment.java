package com.rch.etawah.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.ShopNow_model;
import com.rch.etawah.R;
import com.rch.etawah.util.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

public class Shop_Now_fragment extends Fragment {
    private static String TAG = Shop_Now_fragment.class.getSimpleName();
    private RecyclerView rv_items;
    private List<ShopNow_model> category_modelList = new ArrayList<>();
  //  private shopnow_category_adapter adapter;
    private boolean isSubcat = false;
    String getid;
    String getcat_title;

    LinearLayout search;

    public Shop_Now_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_now, container, false);
        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).setTitle("Shop Now");


        if (ConnectivityReceiver.isConnected()) {
           // makeGetCategoryRequest();

        }

        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_items.setLayoutManager(gridLayoutManager);
       // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);

        //Check Internet Connection

        search = (LinearLayout) view.findViewById(R.id.search1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* SearchFragment fm = new SearchFragment();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel,fm)
                        .addToBackStack(null).commit();
*/
            }
        });

        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = category_modelList.get(position).getId();
                getcat_title = category_modelList.get(position).getTitle();
              /*  Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_12, fm)
                        .addToBackStack(null).commit();
*/
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        return view;
    }


/*
    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";

        isSubcat = false;

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       */
/* if (parent_id != null && parent_id != "") {
        }*//*


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<ShopNow_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new shopnow_category_adapter(category_modelList);
                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                */
/*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }*//*

            }
        });

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
*/

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    // Converting dp to pixel

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
