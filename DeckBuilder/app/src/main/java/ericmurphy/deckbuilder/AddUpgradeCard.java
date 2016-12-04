package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Eric Murphy on 5/3/2016.
 */
public class AddUpgradeCard extends Activity {

    String AffinityOne, AffinityTwo, buttonTag, typeOne, typeTwo, typeThree, typeFour;
    String statsArray;
    Integer listPos;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_grid);

        Intent intent = getIntent();
        AffinityOne = intent.getStringExtra("AffOne");
        AffinityTwo = intent.getStringExtra("AffTwo");
        statsArray = intent.getStringExtra("StatsArray");
        listPos = intent.getIntExtra("Position", 0);
        buttonTag = intent.getStringExtra("ButtonTag");
        typeOne = intent.getStringExtra("TypeOne");
        typeTwo = intent.getStringExtra("TypeTwo");
        typeThree = intent.getStringExtra("TypeThree");
        typeFour = intent.getStringExtra("TypeFour");

        final String[] splitStats = statsArray.split(",");

        //The button to remove the card will only display when a card is already equipped
        Button removeCardButton = (Button)findViewById(R.id.removeUpgradeButton);
        if(buttonTag.equals("upgradeOneBox") && !splitStats[21].equals("UpgradeOneName")) {
            removeCardButton.setVisibility(View.VISIBLE);
        } else if (buttonTag.equals("upgradeTwoBox") && !splitStats[25].equals("UpgradeTwoName")) {
            removeCardButton.setVisibility(View.VISIBLE);
        } else if (buttonTag.equals("upgradeThreeBox") && !splitStats[29].equals("UpgradeThreeName")) {
            removeCardButton.setVisibility(View.VISIBLE);
        }else {
            removeCardButton.setVisibility(View.GONE);
        }

        Button filterButton = (Button)findViewById(R.id.filterButton);
        filterButton.setVisibility(View.GONE);

        Cursor cards = DataBaseHelper.FindUpgradeCards(AffinityOne, AffinityTwo, typeOne, typeTwo, typeThree, typeFour);

        ListView cardView = (ListView) findViewById(R.id.cardList);
        CardAdapter gridAdapter = new CardAdapter(this, cards, 0);
        cardView.setAdapter(gridAdapter);

        //When clicking on an upgrade, it will determine which slot it was intended for
        cardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor cardStats = DataBaseHelper.LoadUpgradeCardStats(v.getTag().toString());
                if (buttonTag.equals("upgradeOneBox")) {
                    if (cardStats.moveToFirst()) {
                        splitStats[21] = cardStats.getString(0);
                        splitStats[22] = cardStats.getString(4);
                        splitStats[23] = cardStats.getString(2);
                        splitStats[24] = cardStats.getString(3);
                    }
                } else if (buttonTag.equals("upgradeTwoBox")) {
                    if (cardStats.moveToFirst()) {
                        splitStats[25] = cardStats.getString(0);
                        splitStats[26] = cardStats.getString(4);
                        splitStats[27] = cardStats.getString(2);
                        splitStats[28] = cardStats.getString(3);
                    }
                } else if (buttonTag.equals("upgradeThreeBox")) {
                    if (cardStats.moveToFirst()) {
                        splitStats[29] = cardStats.getString(0);
                        splitStats[30] = cardStats.getString(4);
                        splitStats[31] = cardStats.getString(2);
                        splitStats[32] = cardStats.getString(3);
                    }
                }
                String recombinedStats = TextUtils.join(",", splitStats);

                AddUpgradeToEquipment(recombinedStats, listPos);
            }
        });
    }

    public void RemoveUpgradeCard(View view) {
        String[] splitStats = statsArray.split(",");

        if (buttonTag.equals("upgradeOneBox")) {
            splitStats[21] = "UpgradeOneName";
            splitStats[22] = "UpgradeOneValue";
            splitStats[23] = "UpgradeOneCost";
            splitStats[24] = "UpgradeOneType";
        } else if (buttonTag.equals("upgradeTwoBox")) {
            splitStats[25] = "UpgradeTwoName";
            splitStats[26] = "UpgradeTwoValue";
            splitStats[27] = "UpgradeTwoCost";
            splitStats[28] = "UpgradeTwoType";
        } else if (buttonTag.equals("upgradeThreeBox")) {
            splitStats[29] = "UpgradeThreeName";
            splitStats[30] = "UpgradeThreeValue";
            splitStats[31] = "UpgradeThreeCost";
            splitStats[32] = "UpgradeThreeType";
        }

        String recombinedStats = TextUtils.join(",", splitStats);

        AddUpgradeToEquipment(recombinedStats, listPos);
    }

    public void AddUpgradeToEquipment(String stats, int position) {
        Intent intent = new Intent(this, DeckBuilder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("UpgradeStats", stats);
        intent.putExtra("UpgradePosition", position);
        startActivity(intent);
    }
}

