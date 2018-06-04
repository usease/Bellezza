package com.example.usease.bellezza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class AddToCartActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mQuantity, mEmail, mPhone, mTelegram;
    private TextView mPrice, mAvailable, mName;
    private String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        mToolbar = (Toolbar) findViewById(R.id.add_to_cart_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_to_cart);

        mQuantity = (TextInputLayout) findViewById(R.id.add_to_cart_quantity);
        mEmail = (TextInputLayout) findViewById(R.id.add_to_cart_email);
        mPhone = (TextInputLayout) findViewById(R.id.add_to_cart_phone_number);
        mPrice = (TextView) findViewById(R.id.add_to_cart_price);
        mName = (TextView) findViewById(R.id.add_to_cart_product_title);
        mAvailable = (TextView) findViewById(R.id.add_to_cart_available);
        mTelegram = (TextInputLayout) findViewById(R.id.add_to_cart_telegram);

        product_id = getIntent().getStringExtra("product_id");
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String available = getIntent().getStringExtra("available");

        mName.setText(name);
        mPrice.setText("$ " + price);
        mAvailable.setText(available);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_new){

            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

            progressDialog.setTitle(R.string.ordering);
            progressDialog.setMessage(getBaseContext().getResources().getString(R.string.processing_order));

            String email = mEmail.getEditText().getText().toString().trim();
            String phone = mPhone.getEditText().getText().toString().trim();
            String quantity = mQuantity.getEditText().getText().toString().trim();
            String telegram = mTelegram.getEditText().getText().toString().trim();

            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(telegram)) {
                Toast.makeText(this, R.string.phone_or_email_provided, Toast.LENGTH_LONG).show();
            } else {
                progressDialog.show();

                final String token_id = FirebaseInstanceId.getInstance().getToken();
                final Map order_map = new HashMap<>();

                order_map.put("product_id", product_id);
                order_map.put("date", FieldValue.serverTimestamp());
                order_map.put("token_id", token_id);

                if(!TextUtils.isEmpty(phone)){
                    phone = "+998" + phone;
                    order_map.put("phone", phone);
                }
                if   (!TextUtils.isEmpty(email)) {
                    order_map.put("email", email);
                }
                if   (!TextUtils.isEmpty(telegram)) {
                    order_map.put("telegram", telegram);
                }
                if (TextUtils.isEmpty(quantity)){
                    order_map.put("quantity", "1");
                } else {
                    order_map.put("quantity", quantity);
                }

                FirebaseFirestore.getInstance().collection("orders").document().set(order_map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(AddToCartActivity.this, R.string.add_cart_success_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddToCartActivity.this, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
