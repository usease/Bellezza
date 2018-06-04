package com.example.usease.bellezza;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.Inet4Address;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Usease on 5/11/2018.
 */

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.ViewHolder> {

    private List<MyOrderModel> mMyOrderModelList;
    private Context context;

    public MyOrdersRecyclerAdapter(List<MyOrderModel> myOrderModels) {
        mMyOrderModelList = myOrderModels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_my_order, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String product_id = mMyOrderModelList.get(position).getProduct_id();

        String quantity = mMyOrderModelList.get(position).getQuantity();
        final int quantity_converted = Integer.parseInt(quantity);
        holder.setQuantity(quantity_converted);

        holder.orderNumberView.setText("" + (position+1) + ".");

        FirebaseFirestore.getInstance().collection("products").document(product_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    String name = documentSnapshot.get("name").toString();
                    holder.setName(name);

                    String price = documentSnapshot.get("price").toString();
                    int price_converted = Integer.parseInt(price);
                    holder.setPrice(price_converted);

                    holder.setTotal(price_converted, quantity_converted);
                }

            }
        });


        holder.removeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(R.string.delete_order)
                        .setMessage(R.string.sure_to_delete_order)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String order_id = mMyOrderModelList.get(position).getOrder_id();
                                FirebaseFirestore.getInstance().collection("orders").document(order_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mMyOrderModelList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, R.string.order_deleted, Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.mipmap.remove_cart_icon)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMyOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView nameView, priceView, quantityView, totalView, orderNumberView;
        private ImageView removeOrderBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            removeOrderBtn = (ImageView) mView.findViewById(R.id.single_my_order_remove_btn);
            orderNumberView = (TextView) mView.findViewById(R.id.single_my_order_order);
        }


        public void setName(String name) {
            nameView = (TextView) mView.findViewById(R.id.single_my_order_name);
            nameView.setText(name);
        }

        public void setQuantity(int quantity){
            quantityView = (TextView) mView.findViewById(R.id.single_my_order_quantity);
            quantityView.setText(""+ quantity);
        }

        public void setPrice(int price) {
            priceView = (TextView) mView.findViewById(R.id.single_my_order_price);
            priceView.setText("$ " + price);
        }

        public void setTotal(int quantity, int price) {
            int total = quantity * price;
            totalView = (TextView) mView.findViewById(R.id.single_my_order_total);
            totalView.setText("$ " + total);
        }


    }
}
