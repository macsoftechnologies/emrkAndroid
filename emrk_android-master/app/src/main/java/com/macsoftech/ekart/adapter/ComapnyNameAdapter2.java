package com.macsoftech.ekart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.model.Vendors;
import com.macsoftech.ekart.model.search.ListOfVendorsData2;

import java.util.List;

public class ComapnyNameAdapter2 extends RecyclerView.Adapter<ComapnyNameAdapter2.MyviewHolder> {

    List<Vendors> companyLists;
    Context mContext;
    View.OnClickListener onItemClickListener;


    public ComapnyNameAdapter2(List<Vendors> list, Context nContext) {
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
         holder.txtcampanyname.setText(companyLists.get(position).getEntityName());
        String mobile = companyLists.get(position).getMobileNum();
        if (mobile == null) {
            mobile = "";
        }
        holder.txtmobileNo.setText("Mobile : " + mobile);
        holder.txtqty.setText("QTY : " + companyLists.get(position).getQuantity());
        holder.txt_vendor_name.setText("Vendor: "+companyLists.get(position).getVendorName());
        holder.txtviewdetails.setTag(holder);
        holder.txtviewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v);
            }
        });


    }

    @Override
    public int getItemCount() {
        return companyLists.size();
    }

    public void clear() {
        if (companyLists != null) {
            companyLists.clear();
            notifyDataSetChanged();
        }
    }

    class MyviewHolder extends RecyclerView.ViewHolder {


        private TextView txtcampanyname;
        private TextView txtmobileNo;
        private TextView txtqty;
        private TextView txtviewdetails;
        private TextView txt_vendor_name;

        public MyviewHolder(View itemView) {
            super(itemView);
            txt_vendor_name = itemView.findViewById(R.id.txt_vendor_name);
            txtcampanyname = itemView.findViewById(R.id.txtcampanyname);
            txtmobileNo = itemView.findViewById(R.id.txtmobileNo);
            txtqty = itemView.findViewById(R.id.txtqty);
            txtviewdetails = itemView.findViewById(R.id.txtviewdetails);

//            itemView.setTag(this);
//            itemView.setOnClickListener(onItemClickListener);

        }
    }

}
