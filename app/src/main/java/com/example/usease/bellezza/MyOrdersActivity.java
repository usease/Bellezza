package com.example.usease.bellezza;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mMyOrdersList;
    private List<MyOrderModel> mMyOrdersModelList;
    private FirebaseFirestore mFirestore;
    private MyOrdersRecyclerAdapter myOrdersRecyclerAdapter;
    private String current_token_id;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        mToolbar = (Toolbar) findViewById(R.id.my_order_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.my_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMyOrdersList = (RecyclerView) findViewById(R.id.my_orders_list);
        mMyOrdersModelList = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
        mProgress = (ProgressBar) findViewById(R.id.my_cart_progress);
        myOrdersRecyclerAdapter = new MyOrdersRecyclerAdapter(mMyOrdersModelList);
        mMyOrdersList.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
        mMyOrdersList.setAdapter(myOrdersRecyclerAdapter);

        current_token_id  = FirebaseInstanceId.getInstance().getToken();

        CollectionReference ordersRef = FirebaseFirestore.getInstance().collection("orders");
        Query query = ordersRef.whereEqualTo("token_id", current_token_id).orderBy("date", Query.Direction.ASCENDING);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                mProgress.setVisibility(View.GONE);
                TextView empty_cart = (TextView) findViewById(R.id.my_cart_empty);

                if (task.getResult().isEmpty()) {

                    empty_cart.setVisibility(View.VISIBLE);

                } else {

                    for ( DocumentSnapshot document : task.getResult().getDocuments()) {

                        MyOrderModel order = document.toObject(MyOrderModel.class).withId(document.getId());
                        mMyOrdersModelList.add(order);
                        myOrdersRecyclerAdapter.notifyDataSetChanged();
                    }
                }



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
