package ericmurphy.deckbuilder;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = null;

    private static String DB_NAME = "DeckDB.db";

    private static SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath().replace("/files","") + "/databases/";
        Log.d("DB PATH", DB_PATH);
    }

    private static final int DATABASE_VERSION = 3;

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.d("DATABASE CHECK", "Database exists, do nothing");
//            File path = new File(DB_PATH + DB_NAME);
//            SQLiteDatabase.deleteDatabase(path);
//        }
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        myDataBase = getWritableDatabase();
        Log.d("DB VERSION", myDataBase.getVersion() + "");
        if (myDataBase.getVersion() == 0) {
            myDataBase.execSQL("PRAGMA user_version = " + DATABASE_VERSION);
            Log.d("DATABASE VERSION", "DATABASE VERSION HAS BEEN INCREASED TO " + myDataBase.getVersion());
        }
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("UPDATE Cards SET Cost = '4' WHERE CardName = 'Honor the Pure'");
        }

        if (oldVersion < 3) {
            db.execSQL("UPDATE Cards SET Cost = '7' WHERE CardName = 'Shockwave'");
            db.execSQL("UPDATE Cards SET StatTypeTwo = '13' WHERE CardName = 'Elysian Diamond'");
            db.execSQL("INSERT into Heroes (Name,AffinityOne,AffinityTwo,Role) VALUES ('Khaimera','Affinity.Fury','Affinity.Growth','Fighter')");
            db.execSQL("UPDATE Cards SET StatValueFour = null, BonusTypeOne = '4', BonusValueOne = '50%' WHERE CardName = 'Heart of the Apex'");
        }
    }

    public static Cursor CheckAffinities(String hero) {
        return myDataBase.query("Heroes", new String[] {"AffinityOne","AffinityTwo"}, "Name = '" + hero + "'", null, null, null, null);
    }

//    public static Cursor FindEquipmentCards(String AffOne, String AffTwo) {
//        return myDataBase.query("Cards", new String[] {"CardName"}, "Affinity in ('" + AffOne + "','" + AffTwo + "','Affinity.Universal') AND Type in ('Active','Passive')", null, null, null, "Cost, CardName");
//    }

    public static Cursor FindFilteredCards(String query) {
        return myDataBase.rawQuery(query, null);
    }

    public static Cursor FindLooseUpgrades(String AffOne, String AffTwo) {
        return myDataBase.query("Cards", new String[] {"CardName"}, "Affinity in ('" + AffOne + "','" + AffTwo + "','Affinity.Universal') AND Type in ('Upgrade')", null, null, null, "Cost, CardName");
    }

    public static Cursor FindUpgradeCards(String AffOne, String AffTwo, String typeOne, String typeTwo, String typeThree, String typeFour) {
        return myDataBase.query("Cards", new String[] {"_id","CardName","Type","Cost","Affinity","StatTypeOne","StatValueOne","StatTypeTwo","StatValueTwo","StatTypeThree","StatValueThree","StatTypeFour","StatValueFour"}, "Affinity in ('" + AffOne + "','" + AffTwo + "','Affinity.Universal') AND Type = 'Upgrade' AND StatTypeOne IN ('" + typeOne + "','" + typeTwo + "','" + typeThree + "','" + typeFour + "')", null, null, null, "Cost, CardName");
    }

    public static Cursor LoadCardStats(String cardName) {
        cardName = cardName.replace("'","''");
        return myDataBase.query("Cards",
                new String[] {"CardName","Type","Cost","StatTypeOne","StatValueOne","StatTypeTwo","StatValueTwo","StatTypeThree","StatValueThree","StatTypeFour","StatValueFour","BonusTypeOne","BonusValueOne","BonusTypeTwo","BonusValueTwo","BonusTypeThree","BonusValueThree","BonusTypeFour","BonusValueFour","CanUpgrade","CanEquip"},
                "CardName = '" + cardName + "'", null, null, null, null);
    }

    public static Cursor LoadCardStatsForDetails(String cardName) {
        cardName = cardName.replace("'","''");
        return myDataBase.query("Cards",
                new String[] {"CardName","Type","Cost","Rarity","Affinity","StatTypeOne","StatValueOne","StatTypeTwo","StatValueTwo","StatTypeThree","StatValueThree","StatTypeFour","StatValueFour","BonusTypeOne","BonusValueOne","BonusTypeTwo","BonusValueTwo","BonusTypeThree","BonusValueThree","BonusTypeFour","BonusValueFour","CanUpgrade","CanEquip","Special"},
                "CardName = '" + cardName + "'", null, null, null, null);
    }

    public static Cursor LoadUpgradeCardStats(String cardName) {
        cardName = cardName.replace("'","''");
        return myDataBase.query("Cards",
                new String[] {"CardName","Type","Cost","StatTypeOne","StatValueOne"},
                "CardName = '" + cardName + "'", null, null, null, null);

    }

    public static void SaveDeckToDatabase(String deckName, String entireDeck) {
        DeleteDeck(deckName);

        String insertQuery = "INSERT INTO Decks VALUES(" +
                entireDeck + ")";
        myDataBase.execSQL(insertQuery);
    }

    public static void DeleteDeck(String deckName) {
        String deleteQuery = "DELETE FROM Decks WHERE DeckName = '" + deckName + "'";
        myDataBase.execSQL(deleteQuery);
    }

    public static Boolean checkForDuplicateDeck(String deckName) {
        Boolean deckExists = false;
        Cursor deckTest = myDataBase.rawQuery("SELECT * FROM Decks WHERE DeckName = '" + deckName + "'",null);
        if (deckTest.moveToFirst()) {
            Log.d("DECK EXIST TEST", deckTest.getString(0));
            if (deckTest.getString(0) == null) {
                deckExists = false;
            } else {
                deckExists = true;
            }
        }
        return deckExists;
    }

    public static Cursor PullSavedDecks() {
        return myDataBase.rawQuery("select rowid _id,DeckName,HeroName from Decks order by _id desc", null);
    }

    public static Cursor SavedDeckDetails(String deckName) {
        return myDataBase.rawQuery("select * from Decks where DeckName = '" + deckName + "'", null);
    }

    public static Cursor FindHeroes() {
        return myDataBase.rawQuery("SELECT _id,Name,Role,AffinityOne,AffinityTwo FROM Heroes ORDER BY Name ASC", null);
    }
}