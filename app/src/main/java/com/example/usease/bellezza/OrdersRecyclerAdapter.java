package com.example.usease.bellezza;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.ViewHolder> {

    private List<OrderModel> mOrderModelList;
    private Context context;
    private CollectionReference mFirestore  = FirebaseFirestore.getInstance().collection("products");

    public OrdersRecyclerAdapter(List<OrderModel> orderModels) {
        mOrderModelList = orderModels;

    }
    @Override
    public OrdersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order, parent, false);


        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrdersRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        String product_id = mOrderModelList.get(position).getProduct_id();
        String quantity = mOrderModelList.get(position).getQuantity();
        holder.setQuantity(quantity);

        Date date = mOrderModelList.get(position).getDate();
        holder.setDate(date);

        String email = mOrderModelList.get(position).getEmail();
        holder.setEmail(email);

        String phone = mOrderModelList.get(position).getPhone();
        holder.setPhone(phone);

        String telegram = mOrderModelList.get(position).getTelegram();
        holder.setTelegram(telegram);

        mFirestore.document(product_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.get("name").toString();
                    holder.setName(name);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mOrderModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView nameView, quantityView, emailView, phoneView, orderNumberView, dateView, telegramView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            nameView = (TextView) mView.findViewById(R.id.single_order_name);
            nameView.setText(name);
        }
        public void setQuantity(String quantity) {
            quantityView = (TextView) mView.findViewById(R.id.single_order_quantity);
            quantityView.setText(quantity);
        }

        public void setEmail(String email) {
            emailView = (TextView) mView.findViewById(R.id.single_order_email);

            if(email != null){
                emailView.setText(email);
            } else {
                emailView.setText(R.string.not_provided);
            }
        }

        public void setPhone(String phone) {
            phoneView = (TextView) mView.findViewById(R.id.single_order_phone);
            if(phone != null){
                phoneView.setText(phone);
            } else {
                phoneView.setText(R.string.not_provided);
            }

        }

        public void setTelegram(String telegram) {
            telegramView = (TextView) mView.findViewById(R.id.single_order_telegram);
            if(telegram != null){
                telegramView.setText(telegram);
            } else {
                telegramView.setText(R.string.not_provided);
            }

        }
        public void setDate(Date date) {

            dateView = (TextView) mView.findViewById(R.id.single_order_date);
                long millisecs = date.getTime();
                String dateFormat = android.text.format.DateFormat.format("MMMM dd, yyyy, HH:MM", new Date(millisecs)).toString();
                dateView.setText(dateFormat);

        }
    }
}
