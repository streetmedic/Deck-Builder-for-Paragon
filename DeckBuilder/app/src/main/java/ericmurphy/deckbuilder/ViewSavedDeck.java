package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by murphyfa on 5/26/2016.
 */
public class ViewSavedDeck extends Activity {

    ArrayList<String> savedStats;
    String deckName, fromAct, heroName, affOne, affTwo, primeCardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_deck_viewer);

        Intent intent = getIntent();
        savedStats = intent.getStringArrayListExtra("DeckArray");
        deckName = intent.getStringExtra("DeckName");
        fromAct = intent.getStringExtra("FromActivity");
        heroName = intent.getStringExtra("HeroName");
        primeCardName = intent.getStringExtra("PrimeCard");

        String[] splitPrime = primeCardName.split(" ");

        ListView listView = (ListView)findViewById(R.id.listView);
        SavedListAdapter savedListAdapter = new SavedListAdapter(this, savedStats);
        Log.d("TESTEST", savedStats.toString());
        listView.setAdapter(savedListAdapter);

        TextView deckNameText = (TextView)findViewById(R.id.savedDeckName);
        deckNameText.setText(deckName);

        Button editButton = (Button)findViewById(R.id.editButton);
        Button homeButton = (Button)findViewById(R.id.homeButton);
        if (fromAct.equals("DeckBuilder")) {
            editButton.setVisibility(View.VISIBLE);
            homeButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.VISIBLE);
            homeButton.setVisibility(View.GONE);
        }

        ImageView heroImage = (ImageView)findViewById(R.id.heroImage);
        heroImage.setImageResource(this.getResources().getIdentifier(heroName.toLowerCase().replace(" ","").replace(".","") + "_icon", "drawable", this.getPackageName()));

        TextView primeName = (TextView)findViewById(R.id.primeName);
        primeName.setText(splitPrime[0] + "\n" + splitPrime[1]);
    }

    public void HomeButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        DeckBuilder.statsArray.clear();
        startActivity(intent);
    }

    public void EditButton(View view) {
        Cursor heroAffs = DataBaseHelper.CheckAffinities(heroName);
        if (heroAffs !=null && heroAffs.moveToFirst()) {
            affOne = heroAffs.getString(0);
            affTwo = heroAffs.getString(1);
        }

        Intent intent = new Intent(this, DeckBuilder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("AFF_ONE", affOne);
        intent.putExtra("AFF_TWO", affTwo);
        intent.putExtra("Hero", heroName);
        intent.putExtra("FromActivity", "ViewSavedDeck");
        intent.putStringArrayListExtra("StatsArray", savedStats);
        intent.putExtra("PrimeCard", primeCardName);
        intent.putExtra("DeckName", deckName);
        startActivity(intent);
    }
}
