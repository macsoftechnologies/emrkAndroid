package com.macsoftech.ekart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.model.search.UserProdResponse;

import java.util.List;

public class ProductNameAdapter extends RecyclerView.Adapter<ProductNameAdapter.MyviewHolder> {

    List<UserProdResponse> companyLists;
    Context mContext;
    View.OnClickListener onItemClickListener;


    public ProductNameAdapter(List<UserProdResponse> list, Context nContext) {
        companyLists = list;
        mContext = nContext;
    }

    public void onItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_company, parent, false);
        return new MyviewHolder(item);
    }


    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        holder.txtcampanyname.setText(companyLists.get(position).getProductName());
        holder.txtmobileNo.setText("Mobile: " + (companyLists.get(position).getMobileNum() == null ? "-" : companyLists.get(position).getMobileNum()));

        holder.txtqty.setText("SIZE: " + companyLists.get(position).getSize());
        holder.txt_vendor_name.setText("Vendor: " + companyLists.get(position).getVendorName());
        holder.txtqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return companyLists.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder {


        private TextView txtcampanyname;
        private TextView txtmobileNo;
        private TextView txtqty;
        private TextView txtviewdetails;
        private TextView txt_vendor_name;

        public MyviewHolder(View itemView) {
            super(itemView);
            txtcampanyname = itemView.findViewById(R.id.txtcampanyname);
            txtmobileNo = itemView.findViewById(R.id.txtmobileNo);
            txtqty = itemView.findViewById(R.id.txtqty);
            txtviewdetails = itemView.findViewById(R.id.txtviewdetails);
            txt_vendor_name = itemView.findViewById(R.id.txt_vendor_name);

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);

        }
    }

}
