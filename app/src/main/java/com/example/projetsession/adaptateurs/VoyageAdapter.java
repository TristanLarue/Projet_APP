package com.example.projetsession.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.modeles.Voyage;
import java.util.List;
import com.bumptech.glide.Glide;

public class VoyageAdapter extends RecyclerView.Adapter<VoyageAdapter.ViewHolder> {
    private List<Voyage> voyageList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Voyage voyage);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VoyageAdapter(List<Voyage> voyageList) {
        this.voyageList = voyageList;
    }

    public void updateList(List<Voyage> list) {
        voyageList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Voyage voyage = voyageList.get(position);
        holder.title.setText(voyage.getNomVoyage());
        holder.summary.setText(voyage.getDescription());
        holder.price.setText("Prix : " + voyage.getPrix() + "$");
        Glide.with(holder.image.getContext())
                .load(voyage.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(voyage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return voyageList == null ? 0 : voyageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, summary, price;
        ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.destinationTitle);
            summary = itemView.findViewById(R.id.destinationSummary);
            price = itemView.findViewById(R.id.destinationPrice);
            image = itemView.findViewById(R.id.destinationImage);
        }
    }
}
