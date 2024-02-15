package android.batch1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {

    Context context;
    ArrayList<SubCategoryList> arrayList;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,outlets,totalDeals,freeDeals;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_sub_category_image);
            name = itemView.findViewById(R.id.custom_sub_category_name);
            outlets = itemView.findViewById(R.id.custom_sub_category_outlets);
            totalDeals = itemView.findViewById(R.id.custom_sub_category_total_deals);
            freeDeals = itemView.findViewById(R.id.custom_sub_category_free_deals);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.outlets.setText(arrayList.get(position).getOutlets()+" outlets nearby");

        if(arrayList.get(position).getTotalDeal()>1) {
            holder.totalDeals.setText("Total Deals : " + arrayList.get(position).getTotalDeal());
        }
        else{
            holder.totalDeals.setText("Total Deal : " + arrayList.get(position).getTotalDeal());
        }

        if(arrayList.get(position).getFreeDeal()>1) {
            holder.freeDeals.setText("Free Deals : " + arrayList.get(position).getFreeDeal());
        }
        else{
            holder.freeDeals.setText("Free Deal : " + arrayList.get(position).getFreeDeal());
        }

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.icon).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
