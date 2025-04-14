package com.example.projetsession.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.dao.VoyageDAO;
import com.example.projetsession.modeles.Reservation;
import com.example.projetsession.modeles.Voyage;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Reservation> reservationList;
    private OnCancelClickListener cancelClickListener;
    private VoyageDAO voyageDAO;

    public interface OnCancelClickListener {
        void onCancelClick(Reservation reservation);
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelClickListener = listener;
    }

    public void setVoyageDAO(VoyageDAO voyageDAO) {
        this.voyageDAO = voyageDAO;
    }

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public void updateList(List<Reservation> list) {
        reservationList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historique, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        String destination = "N/A";
        if (voyageDAO != null) {
            Voyage voyage = voyageDAO.getVoyageParId(reservation.getVoyageId());
            if (voyage != null) {
                destination = voyage.getDestination();
            }
        }
        holder.destinationTextView.setText("Destination: " + destination);
        holder.dateVoyageTextView.setText("Date: " + reservation.getDateReservation());
        holder.montantText.setText("Prix: " + reservation.getMontantTotal() + "$ (" + reservation.getNbPlaces() + " places)");
        holder.statutTextView.setText("Statut: " + reservation.getStatut());
        holder.cancelButton.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(reservation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList == null ? 0 : reservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView, dateVoyageTextView, montantText, statutTextView;
        Button cancelButton;

        public ViewHolder(View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            dateVoyageTextView = itemView.findViewById(R.id.dateVoyageTextView);
            montantText = itemView.findViewById(R.id.montantText);
            statutTextView = itemView.findViewById(R.id.statutTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
