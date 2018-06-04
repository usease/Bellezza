package com.example.usease.bellezza;

import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private List<ProductModel> mWishlistModelList;
    private AllProductsRecyclerAdapter allProductsRecyclerAdapter;
    private boolean isFirstPageLoadedFirst = true;
    private DocumentSnapshot lastVisible;
    private DocumentReference mUserWishlistDoc;
    private RecyclerView mWishlist;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        mToolbar = (Toolbar) findViewById(R.id.wishlist_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_wishlist);

        mProgress = (ProgressBar) findViewById(R.id.wishlist_progress);


        mWishlist = (RecyclerView) findViewById(R.id.wishlist_product_list);
        mWishlistModelList = new ArrayList<>();
        mUserWishlistDoc = FirebaseFirestore.getInstance().collection("wishlist").document(FirebaseInstanceId.getInstance().getToken());

        allProductsRecyclerAdapter = new AllProductsRecyclerAdapter(mWishlistModelList);

        mWishlist.setLayoutManager(new LinearLayoutManager(this));
        mWishlist.setAdapter(allProductsRecyclerAdapter);

        mProgress.setVisibility(View.VISIBLE);

        mUserWishlistDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TextView empty_text = (TextView) findViewById(R.id.wishlist_empty);
                if(!documentSnapshot.exists()) {
                    mProgress.setVisibility(View.INVISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                }

                Map wishlist_map = documentSnapshot.getData();

                if(wishlist_map.isEmpty() || wishlist_map.size() == 0){

                    mProgress.setVisibility(View.INVISIBLE);
                    empty_text.setVisibility(View.VISIBLE);

                } else {
                    mProgress.setVisibility(View.INVISIBLE);
                    empty_text.setVisibility(View.INVISIBLE);
                    for (Object key : wishlist_map.keySet()){
                        addToRecyclerView(key.toString());
                    }
                }
            }
        });
    }

    private void addToRecyclerView(String key) {

        FirebaseFirestore.getInstance().collection("products").document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                ProductModel product = documentSnapshot.toObject(ProductModel.class).withId(documentSnapshot.getId());
                mWishlistModelList.add(product);
                allProductsRecyclerAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

            if (item.getItemId() == android.R.id.home) {
                finish();
            }

            return super.onOptionsItemSelected(item);
        }


}
