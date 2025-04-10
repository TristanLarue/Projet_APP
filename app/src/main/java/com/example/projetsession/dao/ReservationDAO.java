package com.example.projetsession.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projetsession.modeles.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationDAO {
    private SQLiteDatabase database;
    private ReservationDbHelper dbHelper;
    private SimpleDateFormat dateFormat;

    public ReservationDAO(Context context) {
        dbHelper = new ReservationDbHelper(context);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long ajouterReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(ReservationDbHelper.COLUMN_VOYAGE_ID, reservation.getVoyageId());
        values.put(ReservationDbHelper.COLUMN_UTILISATEUR_ID, reservation.getUtilisateurId());
        values.put(ReservationDbHelper.COLUMN_DESTINATION, reservation.getDestination());
        values.put(ReservationDbHelper.COLUMN_DATE_VOYAGE, dateFormat.format(reservation.getDateVoyage()));
        values.put(ReservationDbHelper.COLUMN_NOMBRE_PLACES, reservation.getNombrePlaces());
        values.put(ReservationDbHelper.COLUMN_MONTANT_TOTAL, reservation.getMontantTotal());
        values.put(ReservationDbHelper.COLUMN_STATUT, reservation.getStatut());
        values.put(ReservationDbHelper.COLUMN_DATE_RESERVATION, dateFormat.format(new Date()));
        return database.insert(ReservationDbHelper.TABLE_RESERVATIONS, null, values);
    }

    public List<Reservation> getReservationsUtilisateur(String utilisateurId) {
        List<Reservation> liste = new ArrayList<>();
        String selection = ReservationDbHelper.COLUMN_UTILISATEUR_ID + " = ?";
        String[] selectionArgs = { utilisateurId };
        Cursor cursor = database.query(
                ReservationDbHelper.TABLE_RESERVATIONS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                ReservationDbHelper.COLUMN_DATE_VOYAGE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reservation r = cursorToReservation(cursor);
                liste.add(r);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return liste;
    }

    public int annulerReservation(long reservationId) {
        ContentValues values = new ContentValues();
        values.put(ReservationDbHelper.COLUMN_STATUT, "annulée");
        return database.update(
                ReservationDbHelper.TABLE_RESERVATIONS,
                values,
                ReservationDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reservationId)}
        );
    }

    private Reservation cursorToReservation(Cursor cursor) {
        Reservation reservation = new Reservation();
        reservation.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_ID)));
        reservation.setVoyageId(cursor.getLong(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_VOYAGE_ID)));
        reservation.setUtilisateurId(cursor.getString(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_UTILISATEUR_ID)));
        reservation.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_DESTINATION)));
        try {
            String dateVoyageStr = cursor.getString(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_DATE_VOYAGE));
            Date dateVoyage = dateFormat.parse(dateVoyageStr);
            reservation.setDateVoyage(dateVoyage);
            String dateResStr = cursor.getString(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_DATE_RESERVATION));
            Date dateReservation = dateFormat.parse(dateResStr);
            reservation.setDateReservation(dateReservation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reservation.setNombrePlaces(cursor.getInt(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_NOMBRE_PLACES)));
        reservation.setMontantTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_MONTANT_TOTAL)));
        reservation.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(ReservationDbHelper.COLUMN_STATUT)));
        return reservation;
    }
}
