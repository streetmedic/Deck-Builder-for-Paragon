package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

/**
 * Created by Eric Murphy on 5/1/2016.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle) {
            throw sqle;
        }
    }

    public void NewDeck(View view) {
        Intent intent = new Intent(this, HeroSelect.class);
        startActivity(intent);
    }

    public void ViewSavedDecks(View view) {
        Intent intent = new Intent(this, SavedDecksList.class);
        startActivity(intent);
    }

    public void OpenCardBrowser(View view) {
        Intent intent = new Intent(this, CardBrowser.class);
        startActivity(intent);
    }

    public void SendFeedback(View view) {
        Intent intent = new Intent(this, SendFeedback.class);
        startActivity(intent);
    }
}
