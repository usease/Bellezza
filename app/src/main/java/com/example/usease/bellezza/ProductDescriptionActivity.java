package com.example.usease.bellezza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProductDescriptionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String mProductID;
    private String mName, mPrice, mAvailable;
    private TextView mPriceView, mAvailableView, mDescView, mSpecView, mOtherInfoView, mAdditionalInfoTitle;
    private ImageView mMainImage, mImage1, mImage2, mImage3;
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        mToolbar = (Toolbar) findViewById(R.id.product_description_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        mProgress.setTitle(R.string.loading);
        mProgress.setMessage(getResources().getString(R.string.loading_products));
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mMainImage = (ImageView) findViewById(R.id.product_desc_main_image);
        mImage1 = (ImageView) findViewById(R.id.product_desc_image1);
        mImage2 = (ImageView) findViewById(R.id.product_desc_image2);
        mImage3 = (ImageView) findViewById(R.id.product_desc_image3);
        mPriceView = (TextView) findViewById(R.id.product_desc_price);
        mAvailableView = (TextView) findViewById(R.id.product_desc_available);
        mDescView = (TextView) findViewById(R.id.product_desc_desc);
        mSpecView = (TextView) findViewById(R.id.product_desc_spec);
        mOtherInfoView = (TextView) findViewById(R.id.product_desc_other_info);
        mAdditionalInfoTitle = (TextView) findViewById(R.id.product_desc_additional_info_title);

        //GET PRODUCT NAME AS EXTRAS AND SET IT AS ACTION BAR TITLE ===========================

        mProductID = getIntent().getStringExtra("product_id");

        FirebaseFirestore.getInstance().collection("products").document(mProductID).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                mName = documentSnapshot.get("name").toString();
                mPrice = documentSnapshot.get("price").toString();
                mAvailable = documentSnapshot.get("available").toString();
                getSupportActionBar().setTitle(mName);

                String image1 = null, image2 = null, image3  = null;

                mDescView.setText(documentSnapshot.get("desc").toString());
                mSpecView.setText(documentSnapshot.get("spec").toString());
                mPriceView.setText("$ " + documentSnapshot.get("price").toString());
                mAvailableView.setText("" + documentSnapshot.get("available").toString());

                String main_image_url = documentSnapshot.get("main_image").toString();

                if(documentSnapshot.contains("image1")) {
                    image1 = documentSnapshot.get("image1").toString();
                }
                if (documentSnapshot.contains("image2")) {
                    image2 = documentSnapshot.get("image2").toString();                }

                if(documentSnapshot.contains("image3")){
                    image3 = documentSnapshot.get("image3").toString();
                }
                if(documentSnapshot.contains("additional_info")) {
                    mAdditionalInfoTitle.setVisibility(View.VISIBLE);
                    mOtherInfoView.setVisibility(View.VISIBLE);
                    mOtherInfoView.setText(documentSnapshot.get("additional_info").toString());
                }



                Picasso.get().load(main_image_url).placeholder(R.drawable.no_product).into(mMainImage);

                if(image1 != null) {
                    mImage1.setVisibility(View.VISIBLE);
                    Picasso.get().load(image1).placeholder(R.drawable.no_product).into(mImage1);


                }
                if(image2 != null) {
                    mImage2.setVisibility(View.VISIBLE);
                    Picasso.get().load(image2).placeholder(R.drawable.no_product).into(mImage2);

                }
                if(image3 != null) {
                    mImage3.setVisibility(View.VISIBLE);
                    Picasso.get().load(image3).placeholder(R.drawable.no_product).into(mImage3);

                }
                mProgress.dismiss();
                ConstraintLayout whole_layout = (ConstraintLayout) findViewById(R.id.product_desc_whole_layout);
                whole_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_new) {

            Intent add_to_cart_intent = new Intent (ProductDescriptionActivity.this, AddToCartActivity.class);
            add_to_cart_intent.putExtra("product_id", mProductID);
            add_to_cart_intent.putExtra("name", mName);
            add_to_cart_intent.putExtra("price", mPrice);
            add_to_cart_intent.putExtra("available", mAvailable);
            startActivity(add_to_cart_intent);

       } else if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
