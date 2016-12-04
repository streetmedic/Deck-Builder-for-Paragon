package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by murphyfa on 5/26/2016.
 */
public class SavedDecksList extends Activity {

    DeckListAdapter deckListAdapter = null;
    static Cursor deckCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_decks);

        deckCursor = DataBaseHelper.PullSavedDecks();
        DatabaseUtils.dumpCursor(deckCursor);

        ListView listView = (ListView) findViewById(R.id.savedDeckList);
        deckListAdapter = new DeckListAdapter(this, deckCursor, 0);
        listView.setAdapter(deckListAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete Deck");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Delete Deck")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Toast.makeText(SavedDecksList.this, info.targetView.getTag().toString() + " has been deleted", Toast.LENGTH_SHORT).show();
            DataBaseHelper.DeleteDeck(info.targetView.getTag().toString());
            Cursor updateListCursor = DataBaseHelper.PullSavedDecks();
            deckListAdapter.swapCursor(updateListCursor);
        }
        return true;
    }

    public void LoadSavedDeck(View view) {
        String deckName = view.getTag().toString();
        Cursor deckDetails = DataBaseHelper.SavedDeckDetails(deckName);
        DatabaseUtils.dumpCursor(deckDetails);
        ArrayList<String> deckDetailsList = new ArrayList<>();
        String cardStats = null;
        String heroName = null;
        String PrimeCard = null;

        Integer n = 3;
        if (deckDetails.moveToFirst()) {
            heroName = deckDetails.getString(1);
            PrimeCard = deckDetails.getString(2);

            while (deckDetails.getString(n) != null) {
                Cursor equipmentStats = DataBaseHelper.LoadCardStats(deckDetails.getString(n));
                DatabaseUtils.dumpCursor(equipmentStats);
                if (equipmentStats.moveToFirst()) {
                    cardStats = equipmentStats.getString(0) + "," +
                            equipmentStats.getString(1) + "," +
                            equipmentStats.getString(2) + "," +
                            equipmentStats.getString(3) + "," +
                            equipmentStats.getString(4) + "," +
                            equipmentStats.getString(5) + "," +
                            equipmentStats.getString(6) + "," +
                            equipmentStats.getString(7) + "," +
                            equipmentStats.getString(8) + "," +
                            equipmentStats.getString(9) + "," +
                            equipmentStats.getString(10) + "," +
                            equipmentStats.getString(11) + "," +
                            equipmentStats.getString(12) + "," +
                            equipmentStats.getString(13) + "," +
                            equipmentStats.getString(14) + "," +
                            equipmentStats.getString(15) + "," +
                            equipmentStats.getString(16) + "," +
                            equipmentStats.getString(17) + "," +
                            equipmentStats.getString(18) + "," +
                            equipmentStats.getString(19) + "," +
                            equipmentStats.getString(20);
                }

                if (deckDetails.getString(n + 1) != null) {
                    Cursor upgradeOneStats = DataBaseHelper.LoadUpgradeCardStats(deckDetails.getString(n + 1));
                    if (upgradeOneStats.moveToFirst()) {
                        cardStats = cardStats + "," +
                                upgradeOneStats.getString(0) + "," +
                                upgradeOneStats.getString(4) + "," +
                                upgradeOneStats.getString(2) + "," +
                                upgradeOneStats.getString(3);
                    }
                } else {
                    cardStats = cardStats + ",UpgradeOneName,UpgradeOneValue,UpgradeOneCost,UpgradeOneType";
                }

                if (deckDetails.getString(n + 2) != null) {
                    Cursor upgradeOneStats = DataBaseHelper.LoadUpgradeCardStats(deckDetails.getString(n + 2));
                    if (upgradeOneStats.moveToFirst()) {
                        cardStats = cardStats + "," +
                                upgradeOneStats.getString(0) + "," +
                                upgradeOneStats.getString(4) + "," +
                                upgradeOneStats.getString(2) + "," +
                                upgradeOneStats.getString(3);
                    }
                } else {
                    cardStats = cardStats + ",UpgradeTwoName,UpgradeTwoValue,UpgradeTwoCost,UpgradeTwoType";
                }

                if (deckDetails.getString(n + 3) != null) {
                    Cursor upgradeOneStats = DataBaseHelper.LoadUpgradeCardStats(deckDetails.getString(n + 3));
                    if (upgradeOneStats.moveToFirst()) {
                        cardStats = cardStats + "," +
                                upgradeOneStats.getString(0) + "," +
                                upgradeOneStats.getString(4) + "," +
                                upgradeOneStats.getString(2) + "," +
                                upgradeOneStats.getString(3);
                    }
                } else {
                    cardStats = cardStats + ",UpgradeThreeName,UpgradeThreeValue,UpgradeThreeCost,UpgradeThreeType";
                }

                cardStats = cardStats + ",No";

                deckDetailsList.add(cardStats);
                cardStats = "";
                n = n + 4;
            }
        }

        Log.d("NEW STATS ARRAY", deckDetailsList.toString());

        Intent intent = new Intent(this, ViewSavedDeck.class);

        intent.putExtra("DeckName", deckName);
        intent.putExtra("HeroName", heroName);
        intent.putExtra("PrimeCard", PrimeCard);
        intent.putExtra("FromActivity", "SavedDeckList");
        intent.putStringArrayListExtra("DeckArray", deckDetailsList);

        startActivity(intent);
    }
}