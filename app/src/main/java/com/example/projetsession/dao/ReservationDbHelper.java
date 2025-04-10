package com.example.projetsession.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe SQLiteOpenHelper pour gérer la table des réservations.
 */
public class ReservationDbHelper extends SQLiteOpenHelper {

    // Nom du fichier de base de données
    private static final String DATABASE_NAME = "agence.db";
    // Version de la base (incrémentez si vous modifiez la structure)
    private static final int DATABASE_VERSION = 1;

    // Nom de la table et colonnes
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VOYAGE_ID = "voyage_id";
    public static final String COLUMN_UTILISATEUR_ID = "utilisateur_id";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_DATE_VOYAGE = "date_voyage";
    public static final String COLUMN_NOMBRE_PLACES = "nombre_places";
    public static final String COLUMN_MONTANT_TOTAL = "montant_total";
    public static final String COLUMN_STATUT = "statut"; // "confirmée" ou "annulée"
    public static final String COLUMN_DATE_RESERVATION = "date_reservation";

    // Requête SQL de création de la table
    private static final String CREATE_TABLE_RESERVATIONS =
            "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VOYAGE_ID + " INTEGER NOT NULL, " +
                    COLUMN_UTILISATEUR_ID + " TEXT NOT NULL, " +
                    COLUMN_DESTINATION + " TEXT NOT NULL, " +
                    COLUMN_DATE_VOYAGE + " TEXT NOT NULL, " +
                    COLUMN_NOMBRE_PLACES + " INTEGER NOT NULL, " +
                    COLUMN_MONTANT_TOTAL + " REAL NOT NULL, " +
                    COLUMN_STATUT + " TEXT NOT NULL, " +
                    COLUMN_DATE_RESERVATION + " TEXT NOT NULL" +
                    ");";

    public ReservationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Appelé lors de la création initiale de la base.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESERVATIONS);
    }

    /**
     * Appelé lorsque la version de la base de données change.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime l’ancienne table et la recrée
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }
}
