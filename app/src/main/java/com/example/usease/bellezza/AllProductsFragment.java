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
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllProductsFragment extends Fragment {

    private RecyclerView mAllProductsList;
    private List<ProductModel> mProductsModelList;
    private FirebaseFirestore mFirestore;
    private AllProductsRecyclerAdapter allProductsRecyclerAdapter;
    private DocumentSnapshot lastVisible;
    private boolean isFirstPageLoadedFirst = true;
    private ProgressBar mProgress;
    public AllProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_all_products, container, false);

        mAllProductsList = (RecyclerView) view.findViewById(R.id.all_products_list);
        mProductsModelList = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mProgress = (ProgressBar) view.findViewById(R.id.all_products_progress);

        mAllProductsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if(reachedBottom) {


                    loadMoreProducts();
                }
            }
        });

        allProductsRecyclerAdapter = new AllProductsRecyclerAdapter(mProductsModelList);

        mAllProductsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAllProductsList.setAdapter(allProductsRecyclerAdapter);

        Query firstQuery = mFirestore.collection("products").limit(6);


        firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                TextView empty_products = (TextView) view.findViewById(R.id.all_products_empty);

                if (queryDocumentSnapshots.isEmpty()) {
                    empty_products.bringToFront();
                    empty_products.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                } else {

                    empty_products.setVisibility(View.INVISIBLE);
                    mProgress.setVisibility(View.GONE);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (isFirstPageLoadedFirst) {

                            lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        }

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            ProductModel product = doc.getDocument().toObject(ProductModel.class).withId(doc.getDocument().getId());

                            if (isFirstPageLoadedFirst) {
                                mProductsModelList.add(product);
                            } else {
                                mProductsModelList.add(0, product);
                            }
                            allProductsRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                }

                mProgress.setVisibility(View.GONE);

                isFirstPageLoadedFirst = false;
            }
        });


        return view;
    }

    public void loadMoreProducts() {

        Query nextQuery = mFirestore.collection("products").startAfter(lastVisible).limit(6);

        nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(getContext(), R.string.loading, Toast.LENGTH_SHORT).show();
                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            ProductModel product = doc.getDocument().toObject(ProductModel.class).withId(doc.getDocument().getId());

                            mProductsModelList.add(product);
                            allProductsRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.no_more_products_to_load, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
