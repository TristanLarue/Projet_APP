package com.example.projetsession.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.modeles.Reservation;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> listeReservations;
    private OnReservationClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public ReservationAdapter(List<Reservation> listeReservations, OnReservationClickListener listener) {
        this.listeReservations = listeReservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historique, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = listeReservations.get(position);
        holder.bind(reservation, listener, dateFormat);
    }

    @Override
    public int getItemCount() {
        return (listeReservations != null) ? listeReservations.size() : 0;
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView;
        TextView dateVoyageTextView;
        TextView montantTextView;
        TextView statutTextView;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            dateVoyageTextView = itemView.findViewById(R.id.dateVoyageTextView);
            montantTextView = itemView.findViewById(R.id.montantText);
            statutTextView = itemView.findViewById(R.id.statutTextView);
        }

        public void bind(final Reservation reservation,
                         final OnReservationClickListener listener,
                         final SimpleDateFormat dateFormat) {
            destinationTextView.setText(reservation.getDestination());
            if (reservation.getDateVoyage() != null) {
                dateVoyageTextView.setText(dateFormat.format(reservation.getDateVoyage()));
            } else {
                dateVoyageTextView.setText("N/D");
            }
            montantTextView.setText("Montant payé: " + String.format("%.2f $", reservation.getMontantTotal()));
            statutTextView.setText(reservation.getStatut());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onReservationClick(reservation);
                }
            });
        }
    }

    public interface OnReservationClickListener {
        void onReservationClick(Reservation reservation);
    }
}
