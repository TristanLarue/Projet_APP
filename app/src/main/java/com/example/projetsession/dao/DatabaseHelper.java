package com.example.projetsession.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Reservation;
import com.example.projetsession.modeles.Utilisateur;
import com.example.projetsession.modeles.Voyage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tourisme.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_UTILISATEURS = "utilisateurs";
    private static final String TABLE_VOYAGES = "voyages";
    private static final String TABLE_DATE_VOYAGES = "date_voyages";
    private static final String TABLE_RESERVATIONS = "reservations";

    // Colonnes communes
    private static final String KEY_ID = "id";

    // Colonnes table utilisateurs
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MDP = "mdp";
    private static final String KEY_AGE = "age";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_ADRESSE = "adresse";

    // Colonnes table voyages
    private static final String KEY_NOM_VOYAGE = "nom_voyage";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRIX = "prix";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_DUREE_JOURS = "duree_jours";
    private static final String KEY_TYPE_DE_VOYAGE = "type_de_voyage";
    private static final String KEY_ACTIVITES_INCLUSES = "activites_incluses";

    // Colonnes table date_voyages
    private static final String KEY_VOYAGE_ID = "voyage_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_NB_PLACES_DISPONIBLES = "nb_places_disponibles";

    // Colonnes table reservations
    private static final String KEY_UTILISATEUR_ID = "utilisateur_id";
    private static final String KEY_DATE_VOYAGE_ID = "date_voyage_id";
    private static final String KEY_NB_PLACES = "nb_places";
    private static final String KEY_MONTANT_TOTAL = "montant_total";
    private static final String KEY_STATUT = "statut";
    private static final String KEY_DATE_RESERVATION = "date_reservation";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_UTILISATEURS_TABLE = "CREATE TABLE " + TABLE_UTILISATEURS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"  // For users, you may leave it as is if you insert not an explicit id.
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_MDP + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_TELEPHONE + " TEXT,"
                + KEY_ADRESSE + " TEXT"
                + ")";

        String CREATE_VOYAGES_TABLE = "CREATE TABLE " + TABLE_VOYAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // Changed to AUTOINCREMENT
                + KEY_NOM_VOYAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_PRIX + " REAL,"
                + KEY_DESTINATION + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_DUREE_JOURS + " INTEGER,"
                + KEY_TYPE_DE_VOYAGE + " TEXT,"
                + KEY_ACTIVITES_INCLUSES + " TEXT"
                + ")";

        String CREATE_DATE_VOYAGES_TABLE = "CREATE TABLE " + TABLE_DATE_VOYAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," // You can leave the date table as is if you handle id separately.
                + KEY_VOYAGE_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_NB_PLACES_DISPONIBLES + " INTEGER,"
                + "FOREIGN KEY(" + KEY_VOYAGE_ID + ") REFERENCES " + TABLE_VOYAGES + "(" + KEY_ID + ")"
                + ")";

        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UTILISATEUR_ID + " INTEGER,"
                + KEY_VOYAGE_ID + " INTEGER,"
                + KEY_DATE_VOYAGE_ID + " INTEGER,"
                + KEY_NB_PLACES + " INTEGER,"
                + KEY_MONTANT_TOTAL + " REAL,"
                + KEY_STATUT + " TEXT,"
                + KEY_DATE_RESERVATION + " TEXT,"
                + "FOREIGN KEY(" + KEY_UTILISATEUR_ID + ") REFERENCES " + TABLE_UTILISATEURS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_VOYAGE_ID + ") REFERENCES " + TABLE_VOYAGES + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_DATE_VOYAGE_ID + ") REFERENCES " + TABLE_DATE_VOYAGES + "(" + KEY_ID + ")"
                + ")";

        db.execSQL(CREATE_UTILISATEURS_TABLE);
        db.execSQL(CREATE_VOYAGES_TABLE);
        db.execSQL(CREATE_DATE_VOYAGES_TABLE);
        db.execSQL(CREATE_RESERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE_VOYAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOYAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEURS);
        onCreate(db);
    }

    // Méthodes pour les utilisateurs
    public long ajouterUtilisateur(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NOM, utilisateur.getNom());
        values.put(KEY_PRENOM, utilisateur.getPrenom());
        values.put(KEY_EMAIL, utilisateur.getEmail());
        values.put(KEY_MDP, utilisateur.getMdp());
        values.put(KEY_AGE, utilisateur.getAge());
        values.put(KEY_TELEPHONE, utilisateur.getTelephone());
        values.put(KEY_ADRESSE, utilisateur.getAdresse());

        long id = db.insert(TABLE_UTILISATEURS, null, values);
        db.close();
        return id;
    }

    public Utilisateur getUtilisateur(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UTILISATEURS, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(cursor.getInt(0));
            utilisateur.setNom(cursor.getString(1));
            utilisateur.setPrenom(cursor.getString(2));
            utilisateur.setEmail(cursor.getString(3));
            utilisateur.setMdp(cursor.getString(4));
            utilisateur.setAge(cursor.getInt(5));
            utilisateur.setTelephone(cursor.getString(6));
            utilisateur.setAdresse(cursor.getString(7));

            cursor.close();
            return utilisateur;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public Utilisateur getUtilisateurParEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UTILISATEURS, null, KEY_EMAIL + "=?",
                new String[]{email}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(cursor.getInt(0));
            utilisateur.setNom(cursor.getString(1));
            utilisateur.setPrenom(cursor.getString(2));
            utilisateur.setEmail(cursor.getString(3));
            utilisateur.setMdp(cursor.getString(4));
            utilisateur.setAge(cursor.getInt(5));
            utilisateur.setTelephone(cursor.getString(6));
            utilisateur.setAdresse(cursor.getString(7));

            cursor.close();
            return utilisateur;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Méthodes pour les voyages
    public long ajouterVoyage(Voyage voyage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Do not insert an explicit id; let SQLite auto-generate it.
        values.put(KEY_NOM_VOYAGE, voyage.getNomVoyage());
        values.put(KEY_DESCRIPTION, voyage.getDescription());
        values.put(KEY_PRIX, voyage.getPrix());
        values.put(KEY_DESTINATION, voyage.getDestination());
        values.put(KEY_IMAGE_URL, voyage.getImageUrl());
        values.put(KEY_DUREE_JOURS, voyage.getDureeJours());
        values.put(KEY_TYPE_DE_VOYAGE, voyage.getTypeDeVoyage());
        values.put(KEY_ACTIVITES_INCLUSES, voyage.getActivitesIncluses());

        long id = db.insert(TABLE_VOYAGES, null, values);
        db.close();
        return id;
    }

    public List<Voyage> getAllVoyages() {
        List<Voyage> voyageList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VOYAGES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Voyage voyage = new Voyage();
                voyage.setId(cursor.getInt(0));
                voyage.setNomVoyage(cursor.getString(1));
                voyage.setDescription(cursor.getString(2));
                voyage.setPrix(cursor.getDouble(3));
                voyage.setDestination(cursor.getString(4));
                voyage.setImageUrl(cursor.getString(5));
                voyage.setDureeJours(cursor.getInt(6));
                voyage.setTypeDeVoyage(cursor.getString(7));
                voyage.setActivitesIncluses(cursor.getString(8));

                voyage.setDateVoyages(getDateVoyagesByVoyageId(voyage.getId()));
                voyageList.add(voyage);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return voyageList;
    }

    public Voyage getVoyage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VOYAGES, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Voyage voyage = new Voyage();
            voyage.setId(cursor.getInt(0));
            voyage.setNomVoyage(cursor.getString(1));
            voyage.setDescription(cursor.getString(2));
            voyage.setPrix(cursor.getDouble(3));
            voyage.setDestination(cursor.getString(4));
            voyage.setImageUrl(cursor.getString(5));
            voyage.setDureeJours(cursor.getInt(6));
            voyage.setTypeDeVoyage(cursor.getString(7));
            voyage.setActivitesIncluses(cursor.getString(8));

            voyage.setDateVoyages(getDateVoyagesByVoyageId(voyage.getId()));
            cursor.close();
            return voyage;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Méthodes pour les dates de voyage
    public long ajouterDateVoyage(DateVoyage dateVoyage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_VOYAGE_ID, dateVoyage.getVoyageId());
        values.put(KEY_DATE, dateVoyage.getDate());
        values.put(KEY_NB_PLACES_DISPONIBLES, dateVoyage.getNbPlacesDisponibles());

        long id = db.insert(TABLE_DATE_VOYAGES, null, values);
        db.close();
        return id;
    }

    public List<DateVoyage> getDateVoyagesByVoyageId(int voyageId) {
        List<DateVoyage> dateVoyageList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DATE_VOYAGES, null, KEY_VOYAGE_ID + "=?",
                new String[]{String.valueOf(voyageId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DateVoyage dateVoyage = new DateVoyage();
                dateVoyage.setId(cursor.getInt(0));
                dateVoyage.setVoyageId(cursor.getInt(1));
                dateVoyage.setDate(cursor.getString(2));
                dateVoyage.setNbPlacesDisponibles(cursor.getInt(3));

                dateVoyageList.add(dateVoyage);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return dateVoyageList;
    }

    public void updateNbPlacesDisponibles(int dateVoyageId, int nbPlaces) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NB_PLACES_DISPONIBLES, nbPlaces);
        db.update(TABLE_DATE_VOYAGES, values, KEY_ID + " = ?", new String[]{String.valueOf(dateVoyageId)});
        db.close();
    }

    // Méthodes pour les réservations
    public long ajouterReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_UTILISATEUR_ID, reservation.getUtilisateurId());
        values.put(KEY_VOYAGE_ID, reservation.getVoyageId());
        values.put(KEY_DATE_VOYAGE_ID, reservation.getDateVoyageId());
        values.put(KEY_NB_PLACES, reservation.getNbPlaces());
        values.put(KEY_MONTANT_TOTAL, reservation.getMontantTotal());
        values.put(KEY_STATUT, reservation.getStatut());
        values.put(KEY_DATE_RESERVATION, reservation.getDateReservation());

        long id = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return id;
    }

    public List<Reservation> getReservationsByUtilisateur(int utilisateurId) {
        List<Reservation> reservationList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, KEY_UTILISATEUR_ID + "=?",
                new String[]{String.valueOf(utilisateurId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(0));
                reservation.setUtilisateurId(cursor.getInt(1));
                reservation.setVoyageId(cursor.getInt(2));
                reservation.setDateVoyageId(cursor.getInt(3));
                reservation.setNbPlaces(cursor.getInt(4));
                reservation.setMontantTotal(cursor.getDouble(5));
                reservation.setStatut(cursor.getString(6));
                reservation.setDateReservation(cursor.getString(7));

                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reservationList;
    }

    public void updateReservationStatut(int reservationId, String statut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUT, statut);
        db.update(TABLE_RESERVATIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(reservationId)});
        db.close();
    }
}
