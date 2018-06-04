package com.example.usease.bellezza;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class OrdersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mOrdersList;
    private List<OrderModel> mOrderModelList;
    private OrdersRecyclerAdapter ordersRecyclerAdapter;
    private boolean isFirstPageLoadedFirst = true;
    private DocumentSnapshot lastVisible;
    private FirebaseFirestore mFirestore;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mToolbar = (Toolbar) findViewById(R.id.orders_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = (ProgressBar) findViewById(R.id.orders_progress);

        mOrderModelList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        ordersRecyclerAdapter = new OrdersRecyclerAdapter(mOrderModelList);

        mOrdersList = (RecyclerView) findViewById(R.id.orders_order_list);
        mOrdersList.setLayoutManager(new LinearLayoutManager(this));

        mOrdersList.setAdapter(ordersRecyclerAdapter);


        mOrdersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if(reachedBottom) {


                    loadMoreProducts();
                }
            }
        });

        Query firstQuery = mFirestore.collection("orders").orderBy("date", Query.Direction.DESCENDING).limit(10);
        firstQuery.addSnapshotListener(OrdersActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                TextView empty_cart = (TextView) findViewById(R.id.orders_empty);
                if(queryDocumentSnapshots.isEmpty()) {

                    mProgress.setVisibility(View.GONE);
                    empty_cart.setVisibility(View.VISIBLE);

                } else {
                    mProgress.setVisibility(View.GONE);
                    empty_cart.setVisibility(View.INVISIBLE);
                    for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {

                        if(isFirstPageLoadedFirst) {
                            lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        }

                        if(doc.getType() == DocumentChange.Type.ADDED){
                            OrderModel product = doc.getDocument().toObject(OrderModel.class);
                            if(isFirstPageLoadedFirst) {
                                mOrderModelList.add(product);
                            } else {
                                mOrderModelList.add(0, product);
                            }
                            ordersRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }

                isFirstPageLoadedFirst = false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadMoreProducts() {

        Query nextQuery = mFirestore.collection("orders").orderBy("date", Query.Direction.DESCENDING).startAfter(lastVisible).limit(10);

        nextQuery.addSnapshotListener(OrdersActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(!queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(OrdersActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            OrderModel order = doc.getDocument().toObject(OrderModel.class);
                            mOrderModelList.add(order);
                            ordersRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(OrdersActivity.this, R.string.no_more_orders_to_load, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
