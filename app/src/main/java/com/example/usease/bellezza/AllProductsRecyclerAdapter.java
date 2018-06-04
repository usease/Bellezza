package com.example.usease.bellezza;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usease on 5/11/2018.
 */

public class AllProductsRecyclerAdapter extends RecyclerView.Adapter<AllProductsRecyclerAdapter.ViewHolder> {

    private List<ProductModel> mProductModelList;
    private Context context;
    private DocumentReference mWishListStore;


    public AllProductsRecyclerAdapter(List<ProductModel> productModelList) {
        mProductModelList = productModelList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product, parent, false);
        context = parent.getContext();
        mWishListStore = FirebaseFirestore.getInstance().collection("wishlist").document(FirebaseInstanceId.getInstance().getToken());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String product_id = mProductModelList.get(position).getProduct_id();
        holder.editOrWishlistButton.setImageResource(0);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            holder.editOrWishlistButton.setBackgroundResource(R.mipmap.edit_icon);

        } else {

            mWishListStore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if(documentSnapshot.contains(product_id)){
                        holder.editOrWishlistButton.setTag("liked");
                        holder.editOrWishlistButton.setBackgroundResource(R.mipmap.star_icon_colored);
                    } else {
                        holder.editOrWishlistButton.setTag("not_liked");
                        holder.editOrWishlistButton.setBackgroundResource(R.mipmap.star_icon);
                    }
                }
            });
        }
        holder.editOrWishlistButton.setVisibility(View.VISIBLE);



        final String name = mProductModelList.get(position).getName();
        holder.setName(name);

        String desc = mProductModelList.get(position).getDesc();
        holder.setDesc(desc);

        final int price = mProductModelList.get(position).getPrice();
        holder.setPrice(price);

        final int available = mProductModelList.get(position).getAvailable();
        holder.setAvailable(available);

        String main_image = mProductModelList.get(position).getMain_image();
        holder.setMainImage(main_image);

        Date date = mProductModelList.get(position).getDate();
        holder.setDate(date);

        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add_to_cart_intent = new Intent(context, AddToCartActivity.class);
                add_to_cart_intent.putExtra("product_id", mProductModelList.get(position).getProduct_id());
                add_to_cart_intent.putExtra("name", name);
                add_to_cart_intent.putExtra("price", Integer.toString(price));
                add_to_cart_intent.putExtra("available", Integer.toString(available));
                context.startActivity(add_to_cart_intent);

            }
        });

        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent product_desc_intent = new Intent(context, ProductDescriptionActivity.class);
                product_desc_intent.putExtra("product_id", mProductModelList.get(position).getProduct_id());
                context.startActivity(product_desc_intent);
            }
        });


        holder.editOrWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                    editProduct(position);

                } else {

                   addRemoveFromWishlist(holder, product_id);

                    if(context instanceof WishlistActivity){

                        mProductModelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mProductModelList.size());

                    }

                }


            }


        });
    }

    private void editProduct(int position) {

        Intent edit_intent = new Intent(context, EditProductActivity.class);
        edit_intent.putExtra("product_id", mProductModelList.get(position).getProduct_id());
        edit_intent.putExtra("name", mProductModelList.get(position).getName());
        edit_intent.putExtra("price", mProductModelList.get(position).getPrice());
        edit_intent.putExtra("available", mProductModelList.get(position).getAvailable());
        edit_intent.putExtra("desc", mProductModelList.get(position).getDesc());
        edit_intent.putExtra("main_image", mProductModelList.get(position).getMain_image());
        edit_intent.putExtra("spec", mProductModelList.get(position).getSpec());

        edit_intent.putExtra("additional_info", mProductModelList.get(position).getAdditional_info());
        edit_intent.putExtra("image1", mProductModelList.get(position).getImage1());
        edit_intent.putExtra("image2", mProductModelList.get(position).getImage2());
        edit_intent.putExtra("image3", mProductModelList.get(position).getImage3());

        context.startActivity(edit_intent);
    }

    private void addRemoveFromWishlist(final ViewHolder holder, String product_id) {

        holder.editOrWishlistButton.setImageResource(0);
        //holder.editOrWishlistButton.setEnabled(false);

        if(holder.editOrWishlistButton.getTag() != "liked") {

            holder.editOrWishlistButton.setBackgroundResource(R.mipmap.star_icon_colored);
            holder.editOrWishlistButton.setTag("liked");
            holder.editOrWishlistButton.setEnabled(false);
            final Map wishlist_map = new HashMap<>();
            wishlist_map.put(product_id, FieldValue.serverTimestamp());

            final DocumentReference wishlist_doc = FirebaseFirestore.getInstance().collection("wishlist").document(FirebaseInstanceId.getInstance().getToken());

            wishlist_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.getResult().exists()) {
                        Toast.makeText(context, R.string.added_to_wishlist, Toast.LENGTH_SHORT).show();

                        //if there is already suck document, update it
                        wishlist_doc.update(wishlist_map)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()) {
                                            holder.editOrWishlistButton.setEnabled(true);
                                        } else {
                                            Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, R.string.added_to_wishlist, Toast.LENGTH_SHORT).show();
                        //otherwise create a new one
                        wishlist_doc.set(wishlist_map)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()) {
                                            holder.editOrWishlistButton.setEnabled(true);
                                        } else {
                                            Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });


        } else {

            holder.editOrWishlistButton.setBackgroundResource(R.mipmap.star_icon);
            holder.editOrWishlistButton.setTag("not_liked");
            holder.editOrWishlistButton.setEnabled(false);

            Map wishlist_map = new HashMap<>();
            wishlist_map.put(product_id, FieldValue.delete());

            FirebaseFirestore.getInstance().collection("wishlist").document(FirebaseInstanceId.getInstance().getToken()).update(wishlist_map)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(context, R.string.removed_from_wishlist, Toast.LENGTH_SHORT).show();
                                holder.editOrWishlistButton.setEnabled(true);
                            }
                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        return mProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView nameView, descView, specView, priceView, availableView, additionalInfoView, dateView;
        private ImageView mMainImageView;
        private Button addToCartBtn;
        private ImageView editOrWishlistButton;
        private CardView product_card;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            addToCartBtn = (Button) mView.findViewById(R.id.single_product_add_btn);
            product_card = (CardView) mView.findViewById(R.id.single_product_card);
            editOrWishlistButton = (ImageView) mView.findViewById(R.id.single_product_edit_btn);
        }

        public void setName (String name) {
            nameView = (TextView) mView.findViewById(R.id.single_product_name);
            nameView.setText(name);
        }

        public void setDesc (String desc) {
            descView = (TextView) mView.findViewById(R.id.single_product_desc);
            descView.setText(desc);
        }

        public void setPrice (int price) {
            priceView = (TextView) mView.findViewById(R.id.single_product_price);
            priceView.setText("$ " + price);
        }

        public void setAvailable (int available) {
            availableView = (TextView) mView.findViewById(R.id.single_product_available);
            availableView.setText(""+ available);


        }

        public void setMainImage (String main_image){
            mMainImageView = (ImageView) mView.findViewById(R.id.single_product_main_image);
            Picasso.get().load(main_image).placeholder(R.drawable.no_product).error(R.drawable.no_product).into(mMainImageView);
        }

        public void setDate (Date date) {

            if(date != null) {
                long millisecs = date.getTime();
                String dateFormat = android.text.format.DateFormat.format("MMMM dd, yyyy", new Date(millisecs)).toString();
                dateView = (TextView) mView.findViewById(R.id.single_product_date);
                dateView.setText(dateFormat);
            }

        }
    }
}
