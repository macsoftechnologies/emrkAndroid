package com.macsoftech.ekart.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macsoftech.ekart.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 */

public class NotificationsAdapter extends BaseRecycleAdapter<NotificationsModel> {


    public NotificationsAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_notifications, parent, false);
        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ProductsViewHolder viewHolder = (ProductsViewHolder) holder;

        viewHolder.itemView.setOnClickListener(v -> {
            try {
//                if (mContext instanceof NotificationsActivity) {
//                    ((NotificationsActivity) mContext).redirectToNext(getItem(position).getDeepLinkID(), getItem(position).getDeepLinkKey());
//                }
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(getItem(position).getDeepLinkKey()));
//                intent.putExtra("pn_id", getItem(position).getDeepLinkID());
//                mContext.startActivity(intent);
            } catch (Exception e) {
                //
            }
        });
        viewHolder.txtTitle.setText(getItem(position).getTitle());
        viewHolder.txt_description.setText(getItem(position).getBody());
        viewHolder.txt_date.setText(getRelativeTime(getItem(position).getCreatedAt()));
    }

    String getRelativeTime(String date) {
        //2022-09-03T08:35:00.811Z
        date = date.substring(0, date.lastIndexOf("."));
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date now = dft.parse(date);
            long time = now.getTime();///System.currentTimeMillis();
            return String.valueOf(DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle;
        final TextView txt_description;
        final TextView txt_date;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }
}
