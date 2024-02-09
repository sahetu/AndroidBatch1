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

public class MyntraCategoryAdapter extends RecyclerView.Adapter<MyntraCategoryAdapter.MyHolder> {

    Context context;
    ArrayList<MyntraList> arrayList;

    public MyntraCategoryAdapter(Context context, ArrayList<MyntraList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_myntra,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name,offer;
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_myntra_name);
            offer = itemView.findViewById(R.id.custom_myntra_offer);
            imageView = itemView.findViewById(R.id.custom_myntra_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.offer.setText(arrayList.get(position).getOffer());
        //holder.imageView.setImageResource(arrayList.get(position).getImage());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.icon).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
