package com.example.usease.bellezza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddNewProductActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mMainImage, mImage1, mImage2, mImage3;
    private EditText  mDesc, mSpec, mAdditionalInfo;
    private ProgressBar mProgress;
    private static final int MAIN_IMAGE_PICK_CODE = 1; //Code to start Gallery Intent
    private static final int FIRST_IMAGE_PICK_CODE = 2; //Code to start Gallery Intent
    private static final int SECOND_IMAGE_PICK_CODE = 3; //Code to start Gallery Intent
    private static final int THIRD_IMAGE_PICK_CODE = 4; //Code to start Gallery Intent
    private String click_type;
    private Uri mMainImageUri = null;
    private Uri mImage1Uri = null;
    private Uri mImage2Uri = null;
    private Uri mImage3Uri = null;
    private ScrollView mScrollView;

    private TextInputLayout mName, mPrice, mAvailable;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        int random_int = 0;
        random_int = random_int+15;

        mToolbar = (Toolbar) findViewById(R.id.add_new_product_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_new_product);

        mMainImage = (ImageView) findViewById(R.id.product_desc_main_image);
        mImage1 = (ImageView) findViewById(R.id.product_desc_image1);
        mImage2 = (ImageView) findViewById(R.id.product_desc_image2);
        mImage3 = (ImageView) findViewById(R.id.product_desc_image3) ;
        mName = (TextInputLayout) findViewById(R.id.add_new_product_name);
        mDesc = (EditText) findViewById(R.id.add_new_product_desc);
        mSpec = (EditText) findViewById(R.id.add_new_product_specs);
        mPrice = (TextInputLayout) findViewById(R.id.add_new_product_price);
        mAvailable = (TextInputLayout) findViewById(R.id.add_new_product_available);
        mAdditionalInfo = (EditText) findViewById(R.id.add_new_product_additional_info);
        mProgress = (ProgressBar) findViewById(R.id.add_new_product_progress);
        mScrollView = (ScrollView) findViewById(R.id.scrollView2);

        mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "main";
                //When user clicks change image button, creating new intent
                Intent gallery_intent = new Intent();
                //Setting the type to image so that we can pick only images
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                //Initializing Default File Chooser of the phone
                startActivityForResult(Intent.createChooser(gallery_intent, getResources().getString(R.string.select_image)), MAIN_IMAGE_PICK_CODE);
            }
        });

        mImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "image1";
                //When user clicks change image button, creating new intent
                Intent gallery_intent = new Intent();
                //Setting the type to image so that we can pick only images
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                //Initializing Default File Chooser of the phone
                startActivityForResult(Intent.createChooser(gallery_intent, getResources().getString(R.string.select_image)), FIRST_IMAGE_PICK_CODE);

            }
        });
        mImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "image2";
                //When user clicks change image button, creating new intent
                Intent gallery_intent = new Intent();
                //Setting the type to image so that we can pick only images
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                //Initializing Default File Chooser of the phone
                startActivityForResult(Intent.createChooser(gallery_intent, getResources().getString(R.string.select_image)), SECOND_IMAGE_PICK_CODE);

            }
        });
        mImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_type = "image3";
                //When user clicks change image button, creating new intent
                Intent gallery_intent = new Intent();
                //Setting the type to image so that we can pick only images
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                //Initializing Default File Chooser of the phone
                startActivityForResult(Intent.createChooser(gallery_intent, getResources().getString(R.string.select_image)), THIRD_IMAGE_PICK_CODE);
            }
        });


        mSpec.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mAdditionalInfo.clearFocus();
                    TextView spec_title = (TextView) findViewById(R.id.textView11);
                    mScrollView.scrollTo(0,spec_title.getBottom()-50);
                }
            }
        });

        mAdditionalInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(mAdditionalInfo.hasFocus()) {
                    //For some reasons, scrolling nees to be
                    mScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },500);
                }

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_announcement_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save_announcement_btn) {

            String name = mName.getEditText().getText().toString().trim();
            String desc = mDesc.getText().toString().trim();
            String spec = mSpec.getText().toString().trim();
            String additional_info = mAdditionalInfo.getText().toString().trim();
            String price = mPrice.getEditText().getText().toString().trim();
            String available = mAvailable.getEditText().getText().toString().trim();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(spec) && !TextUtils.isEmpty(price) && mMainImage.getTag() == "selected" ) {

                   //Adding product to Firestore
                    addNewProduct(name, desc, spec, price, additional_info, available);


            } else {
                String warning_message = "";
                if(TextUtils.isEmpty(name)) {
                    if(TextUtils.isEmpty(warning_message)) {
                        warning_message += getResources().getString(R.string.product_name_warning);
                    } else {
                        warning_message += ", " + getResources().getString(R.string.product_name_warning);
                    }
                }

                if(TextUtils.isEmpty(desc)) {

                    if(TextUtils.isEmpty(warning_message)) {
                        warning_message += getResources().getString(R.string.product_desc_warning);
                    } else {
                        warning_message += ", " + getResources().getString(R.string.product_desc_warning);
                    }
                }

                if(TextUtils.isEmpty(spec)) {

                    if(TextUtils.isEmpty(warning_message)) {
                        warning_message += getResources().getString(R.string.product_spec_warning);
                    } else {
                        warning_message += ", " + getResources().getString(R.string.product_spec_warning);
                    }
                }



                if(TextUtils.isEmpty(price)) {

                    if(TextUtils.isEmpty(warning_message)) {
                        warning_message +=  getResources().getString(R.string.product_price_warning);
                    } else {
                        warning_message += ", " + getResources().getString(R.string.product_price_warning);
                    }
                }


                if(mMainImage.getTag() != "selected") {

                    if(TextUtils.isEmpty(warning_message)) {
                        warning_message += getResources().getString(R.string.main_image_warning);
                    } else {
                        warning_message += ", " + getResources().getString(R.string.main_image_warning);
                    }
                }

                warning_message +=  " " + getResources().getString(R.string.cant_be_empty);

                Toast.makeText(this, warning_message, Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewProduct(final String name, final String desc, final String spec, final String price, final String additional_info, final String available) {
        mProgress.setVisibility(View.VISIBLE);
        mProgress.bringToFront();

        final Map product_map = new HashMap<>();
        product_map.put("name", name);
        product_map.put("desc", desc);
        product_map.put("spec", spec);
        product_map.put("price", Integer.parseInt(price));
        product_map.put("date", FieldValue.serverTimestamp());



        if(!TextUtils.isEmpty(available)) {
            product_map.put("available", Integer.parseInt(available));
        } else {
            product_map.put("available", 0);
        }

        if(!TextUtils.isEmpty(additional_info)) {
            product_map.put("additional_info", additional_info);
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


            // auto-generate ID
            DocumentReference noteDocRef = firestore.collection("products").document();

            final String product_id = noteDocRef.getId();

            firestore.collection("products").document(product_id).set(product_map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    uploadImages(product_map, product_id);
                }
            });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(ContextCompat.checkSelfPermission(AddNewProductActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                recieveImageResults(requestCode, resultCode, RESULT_OK, data);
            }
        } else {
            recieveImageResults(requestCode, resultCode, RESULT_OK, data);
        }








    }

    private void recieveImageResults(int requestCode,int resultCode, int resultOk, Intent data) {
        if (requestCode == MAIN_IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            //Getting reference to the image which had been selected and making it source file uri
            final Uri sourceUri = data.getData();
            //The cropped image will be saved in the cache for a while. Thus, creating temporary file to hold cropped image
            File tempCropped = new File (getCacheDir(), "main_image");
            //This file's URI is used to upload the cropped image. Thus, getting URI of the file.
            Uri destinationUri = Uri.fromFile(tempCropped);
            //Calling UCROP library to crop the image. NOTE: WE PROVIDE THE SOURCE AND DESTINATION URIs.
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(6, 5)  //Certain formatting to disable users from uploading too large or small files
                    .withMaxResultSize(800, 600)
                    .start(this);
            TextView main_image_hint = (TextView) findViewById(R.id.add_new_product_main_image_pick_hint);
            main_image_hint.setVisibility(View.INVISIBLE);
            mMainImage.setImageURI(sourceUri);
            mMainImage.setTag("selected");
        }




        if (requestCode == FIRST_IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            //Getting reference to the image which had been selected and making it source file uri
            final Uri sourceUri = data.getData();
            //The cropped image will be saved in the cache for a while. Thus, creating temporary file to hold cropped image
            File tempCropped = new File (getCacheDir(), "image1");
            //This file's URI is used to upload the cropped image. Thus, getting URI of the file.
            Uri destinationUri = Uri.fromFile(tempCropped);
            //Calling UCROP library to crop the image. NOTE: WE PROVIDE THE SOURCE AND DESTINATION URIs.
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(6, 5)  //Certain formatting to disable users from uploading too large or small files
                    .withMaxResultSize(800, 600)
                    .start(this);
            TextView first_image_hint = (TextView) findViewById(R.id.add_new_product_image1_pick_hint);
            first_image_hint.setVisibility(View.INVISIBLE);
            mImage1.setImageURI(sourceUri);
            mImage1.setTag("selected");

        }

        if (requestCode == SECOND_IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            //Getting reference to the image which had been selected and making it source file uri
            final Uri sourceUri = data.getData();
            //The cropped image will be saved in the cache for a while. Thus, creating temporary file to hold cropped image
            File tempCropped = new File (getCacheDir(), "image2");
            //This file's URI is used to upload the cropped image. Thus, getting URI of the file.
            Uri destinationUri = Uri.fromFile(tempCropped);
            //Calling UCROP library to crop the image. NOTE: WE PROVIDE THE SOURCE AND DESTINATION URIs.
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(6, 5)  //Certain formatting to disable users from uploading too large or small files
                    .withMaxResultSize(800, 600)
                    .start(this);
            TextView second_image_hint = (TextView) findViewById(R.id.add_new_product_image2_pick_hint);
            second_image_hint.setVisibility(View.INVISIBLE);
            mImage2.setImageURI(sourceUri);
            mImage2.setTag("selected");
        }

        if (requestCode == THIRD_IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            //Getting reference to the image which had been selected and making it source file uri
            final Uri sourceUri = data.getData();
            //The cropped image will be saved in the cache for a while. Thus, creating temporary file to hold cropped image
            File tempCropped = new File (getCacheDir(), "image3");
            //This file's URI is used to upload the cropped image. Thus, getting URI of the file.
            Uri destinationUri = Uri.fromFile(tempCropped);
            //Calling UCROP library to crop the image. NOTE: WE PROVIDE THE SOURCE AND DESTINATION URIs.
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(6, 5)  //Certain formatting to disable users from uploading too large or small files
                    .withMaxResultSize(800, 600)
                    .start(this);
            TextView third_image_hint = (TextView) findViewById(R.id.add_new_product_image3_pick_hint);
            third_image_hint.setVisibility(View.INVISIBLE);
            mImage3.setImageURI(sourceUri);
            mImage3.setTag("selected");

        }

        if(requestCode == UCrop.REQUEST_CROP ) {
            //If image is successfully cropped, getting the resulting URI of the cropped image
            Uri imgUri = UCrop.getOutput(data);

            if (click_type.equalsIgnoreCase("main")) {
                mMainImage.setImageURI(imgUri);
                mMainImageUri = imgUri;


            }
            if (click_type.equalsIgnoreCase("image1")) {

                mImage1.setImageURI(imgUri);
                mImage1Uri = imgUri;

            }
            if (click_type.equalsIgnoreCase("image2")) {

                mImage2.setImageURI(imgUri);
                mImage2Uri = imgUri;

            }
            if (click_type.equalsIgnoreCase("image3")) {

                mImage3.setImageURI(imgUri);
                mImage3Uri = imgUri;


            }
        }

    }


    private void uploadImages(final Map product_map, final String product_id) {
        final StorageReference product_images_storage_reference = FirebaseStorage.getInstance().getReference().child("product_images").child(product_id);

         product_images_storage_reference.child("main_image").putFile(mMainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String main_image_download_url = taskSnapshot.getDownloadUrl().toString();
                    product_map.put("main_image", main_image_download_url);

                    FirebaseFirestore.getInstance().collection("products").document(product_id).update(product_map).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            uploadRemainingImages(product_images_storage_reference, product_map, product_id);
                        }
                    });


                    mProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.added_product_successfully, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void uploadRemainingImages(StorageReference product_images_storage_reference, final Map product_map, final String product_id) {


        if(mImage1Uri != null) {
            product_images_storage_reference.child("image1").putFile(mImage1Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String image1_download_url = taskSnapshot.getDownloadUrl().toString();
                    product_map.put("image1", image1_download_url);
                    uploadAdditionalData(product_id, product_map);

                }
            });

        }
        if(mImage2Uri != null) {
            product_images_storage_reference.child("image2").putFile(mImage2Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String image2_download_url = taskSnapshot.getDownloadUrl().toString();
                    product_map.put("image2", image2_download_url);
                    uploadAdditionalData(product_id, product_map);
                }
            });

        }
        if(mImage3Uri != null) {
            product_images_storage_reference.child("image3").putFile(mImage3Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String image3_download_url = taskSnapshot.getDownloadUrl().toString();
                    product_map.put("image3", image3_download_url);

                    uploadAdditionalData(product_id, product_map);
                }
            });

        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 2000);
    }

    public void uploadAdditionalData(String product_id, Map product_map) {
        FirebaseFirestore.getInstance().collection("products").document(product_id).update(product_map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        });
    }

}
