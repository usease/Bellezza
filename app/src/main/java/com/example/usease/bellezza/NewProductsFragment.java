package com.example.usease.bellezza;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductsFragment extends Fragment {

    private RecyclerView mAllProductsList;
    private List<ProductModel> mProductsModelList;
    private FirebaseFirestore mFirestore;
    private AllProductsRecyclerAdapter allProductsRecyclerAdapter;
    private ProgressBar mProgress;


    public NewProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_new_products, container, false);

        mAllProductsList = (RecyclerView) view.findViewById(R.id.all_products_list);
        mProductsModelList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mProgress = (ProgressBar) view.findViewById(R.id.new_products_progress);

        allProductsRecyclerAdapter = new AllProductsRecyclerAdapter(mProductsModelList);

        mAllProductsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAllProductsList.setAdapter(allProductsRecyclerAdapter);

        CollectionReference productsRef = FirebaseFirestore.getInstance().collection("products");
        Query query = productsRef.orderBy("date", Query.Direction.DESCENDING).limit(9);

        query.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                TextView empty_products = (TextView) view.findViewById(R.id.new_products_empty);
                if(queryDocumentSnapshots.isEmpty() ){
                    empty_products.bringToFront();
                    empty_products.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                } else {
                    empty_products.setVisibility(View.INVISIBLE);
                    mProgress.setVisibility(View.GONE);

                    for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {

                        if(doc.getType() == DocumentChange.Type.ADDED){

                            ProductModel product = doc.getDocument().toObject(ProductModel.class).withId(doc.getDocument().getId());

                            mProductsModelList.add(product);
                            allProductsRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                }
            }
        });


        return view;
    }



}
