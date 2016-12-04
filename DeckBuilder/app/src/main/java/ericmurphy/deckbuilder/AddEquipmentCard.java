package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;


/**
 * Created by Eric Murphy on 5/3/2016.
 */
public class AddEquipmentCard extends Activity {

    CardAdapter cardAdapter = null;

    String AffinityOne, AffinityTwo, ButtonPress, cardQuery;
    Cursor cards;

    String filterQuery = "";
    String queryStart;
    String queryEnd = " ORDER BY Cost ASC, CardName ASC";

    Boolean showFilters = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_grid);

        Intent intent = getIntent();
        AffinityOne = intent.getStringExtra("AffinityOne");
        AffinityTwo = intent.getStringExtra("AffinityTwo");
        ButtonPress = intent.getStringExtra("ButtonPress");

        //This checks for which activity this activity opened from so we pull the right cards, equipment or upgrades
        if (ButtonPress.equals("equipmentButton")) {
            queryStart = "SELECT _id,CardName,Type,Cost,Affinity,StatTypeOne,StatValueOne,StatTypeTwo,StatValueTwo,StatTypeThree,StatValueThree,StatTypeFour,StatValueFour FROM Cards WHERE Type IN ('Passive','Active') AND Affinity IN ('Affinity.Universal','" + AffinityOne + "','" + AffinityTwo + "')";
        } else {
            queryStart = "SELECT _id,CardName,Type,Cost,Affinity,StatTypeOne,StatValueOne,StatTypeTwo,StatValueTwo,StatTypeThree,StatValueThree,StatTypeFour,StatValueFour FROM Cards WHERE Type IN ('Upgrade') AND Affinity IN ('Affinity.Universal','" + AffinityOne + "','" + AffinityTwo + "')";
        }

        cardQuery = queryStart + queryEnd;

        cards = DataBaseHelper.FindFilteredCards(cardQuery);

        ListView cardView = (ListView) findViewById(R.id.cardList);
        cardAdapter = new CardAdapter(this, cards, 0);
        cardView.setAdapter(cardAdapter);

        //Determine if we are adding equipment or upgrade when the user clicks on a card
        cardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String dbCardName = v.getTag().toString();
                String editCardName = dbCardName.toLowerCase().replace("'","").replace(" ","_");
                if (ButtonPress.equals("equipmentButton")) {
                    LoadEquipmentDetails(editCardName, dbCardName);
                } else if (ButtonPress.equals("upgradeButton")) {
                    LoadUpgradeDetails(dbCardName);
                }
            }
        });
    }

    //Clicking equipment card loads the tooltip page
    public void LoadEquipmentDetails(String cardClicked, String dbCardClicked) {
        Intent intent = new Intent(this, EquipmentDetails.class);
        intent.putExtra("CardName", cardClicked);
        intent.putExtra("DatabaseName", dbCardClicked);
        startActivity(intent);
    }

    //Clicking upgrade card immediately equips it
    public void LoadUpgradeDetails(String dbCardClicked) {
        Intent intent = new Intent(this, DeckBuilder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("DatabaseName", dbCardClicked);
        intent.putExtra("Equipped", "No");
        startActivity(intent);
    }

    public void ShowHideFilters(View view) {
        FrameLayout filterBox = (FrameLayout)findViewById(R.id.filterBox);
        if (showFilters) {
            filterBox.setVisibility(View.GONE);
            showFilters = false;
        } else {
            filterBox.setVisibility(View.VISIBLE);
            showFilters = true;
        }
    }

    public void FilterCards(View view) {
        CheckBox statCB = (CheckBox)view.findViewWithTag(view.getTag());

        if (statCB.isChecked()) {
            switch (view.getTag().toString()) {
                case "energyDamageCB" : filterQuery = filterQuery + " and (stattypeone = '0' or stattypetwo = '0' or stattypethree = '0' or stattypefour = '0' or bonustypeone = '0' or bonustypetwo = '0' or bonustypethree = '0' or bonustypefour = '0')";
                    break;
                case "physicalDamageCB" : filterQuery = filterQuery + " and (stattypeone = '1' or stattypetwo = '1' or stattypethree = '1' or stattypefour = '1' or bonustypeone = '1' or bonustypetwo = '1' or bonustypethree = '1' or bonustypefour = '1')";
                    break;
                case "critChanceCB" : filterQuery = filterQuery + " and (stattypeone = '3' or stattypetwo = '3' or stattypethree = '3' or stattypefour = '3' or bonustypeone = '3' or bonustypetwo = '3' or bonustypethree = '3' or bonustypefour = '3')";
                    break;
                case "maxHPCB" : filterQuery = filterQuery + " and (stattypeone = '10' or stattypetwo = '10' or stattypethree = '10' or stattypefour = '10' or bonustypeone = '10' or bonustypetwo = '10' or bonustypethree = '10' or bonustypefour = '10')";
                    break;
                case "hpRegenCB" : filterQuery = filterQuery + " and (stattypeone = '11' or stattypetwo = '11' or stattypethree = '11' or stattypefour = '11' or bonustypeone = '11' or bonustypetwo = '11' or bonustypethree = '11' or bonustypefour = '11')";
                    break;
                case "energyArmorCB" : filterQuery = filterQuery + " and (stattypeone = '8' or stattypetwo = '8' or stattypethree = '8' or stattypefour = '8' or bonustypeone = '8' or bonustypetwo = '8' or bonustypethree = '8' or bonustypefour = '8')";
                    break;
                case "physicalArmorCB" : filterQuery = filterQuery + " and (stattypeone = '6' or stattypetwo = '6' or stattypethree = '6' or stattypefour = '6' or bonustypeone = '6' or bonustypetwo = '6' or bonustypethree = '6' or bonustypefour = '6')";
                    break;
                case "critBonusCB" : filterQuery = filterQuery + " and (bonustypeone = '4' or bonustypetwo = '4' or bonustypethree = '4' or bonustypefour = '4' or bonustypeone = '4' or bonustypetwo = '4' or bonustypethree = '4' or bonustypefour = '4')";
                    break;
                case "maxManaCB" : filterQuery = filterQuery + " and (stattypeone = '12' or stattypetwo = '12' or stattypethree = '12' or stattypefour = '12' or bonustypeone = '12' or bonustypetwo = '12' or bonustypethree = '12' or bonustypefour = '12')";
                    break;
                case "manaRegenCB" : filterQuery = filterQuery + " and (stattypeone = '13' or stattypetwo = '13' or stattypethree = '13' or stattypefour = '13' or bonustypeone = '13' or bonustypetwo = '13' or bonustypethree = '13' or bonustypefour = '13')";
                    break;
                case "energyPenCB" : filterQuery = filterQuery + " and (stattypeone = '9' or stattypetwo = '9' or stattypethree = '9' or stattypefour = '9' or bonustypeone = '9' or bonustypetwo = '9' or bonustypethree = '9' or bonustypefour = '9')";
                    break;
                case "physicalPenCB" : filterQuery = filterQuery + " and (stattypeone = '7' or stattypetwo = '7' or stattypethree = '7' or stattypefour = '7' or bonustypeone = '7' or bonustypetwo = '7' or bonustypethree = '7' or bonustypefour = '7')";
                    break;
                case "attackSpeedCB" : filterQuery = filterQuery + " and (stattypeone = '2' or stattypetwo = '2' or stattypethree = '2' or stattypefour = '2' or bonustypeone = '2' or bonustypetwo = '2' or bonustypethree = '2' or bonustypefour = '2')";
                    break;
                case "lifestealCB" : filterQuery = filterQuery + " and (stattypeone = '5' or stattypetwo = '5' or stattypethree = '5' or stattypefour = '5' or bonustypeone = '5' or bonustypetwo = '5' or bonustypethree = '5' or bonustypefour = '5')";
                    break;
                case "cooldownCB" : filterQuery = filterQuery + " and (stattypeone = '14' or stattypetwo = '14' or stattypethree = '14' or stattypefour = '14' or bonustypeone = '14' or bonustypetwo = '14' or bonustypethree = '14' or bonustypefour = '14')";
                    break;
            }
        } else {
            switch (view.getTag().toString()) {
                case "energyDamageCB" : filterQuery = filterQuery.replace(" and (stattypeone = '0' or stattypetwo = '0' or stattypethree = '0' or stattypefour = '0' or bonustypeone = '0' or bonustypetwo = '0' or bonustypethree = '0' or bonustypefour = '0')","");
                    break;
                case "physicalDamageCB" : filterQuery = filterQuery.replace(" and (stattypeone = '1' or stattypetwo = '1' or stattypethree = '1' or stattypefour = '1' or bonustypeone = '1' or bonustypetwo = '1' or bonustypethree = '1' or bonustypefour = '1')","");
                    break;
                case "critChanceCB" : filterQuery = filterQuery.replace(" and (stattypeone = '3' or stattypetwo = '3' or stattypethree = '3' or stattypefour = '3' or bonustypeone = '3' or bonustypetwo = '3' or bonustypethree = '3' or bonustypefour = '3')","");
                    break;
                case "maxHPCB" : filterQuery = filterQuery.replace(" and (stattypeone = '10' or stattypetwo = '10' or stattypethree = '10' or stattypefour = '10' or bonustypeone = '10' or bonustypetwo = '10' or bonustypethree = '10' or bonustypefour = '10')","");
                    break;
                case "hpRegenCB" : filterQuery = filterQuery.replace(" and (stattypeone = '11' or stattypetwo = '11' or stattypethree = '11' or stattypefour = '11' or bonustypeone = '11' or bonustypetwo = '11' or bonustypethree = '11' or bonustypefour = '11')","");
                    break;
                case "energyArmorCB" : filterQuery = filterQuery.replace(" and (stattypeone = '8' or stattypetwo = '8' or stattypethree = '8' or stattypefour = '8' or bonustypeone = '8' or bonustypetwo = '8' or bonustypethree = '8' or bonustypefour = '8')","");
                    break;
                case "physicalArmorCB" : filterQuery = filterQuery.replace(" and (stattypeone = '6' or stattypetwo = '6' or stattypethree = '6' or stattypefour = '6' or bonustypeone = '6' or bonustypetwo = '6' or bonustypethree = '6' or bonustypefour = '6')","");
                    break;
                case "critBonusCB" : filterQuery = filterQuery.replace(" and (bonustypeone = '4' or bonustypetwo = '4' or bonustypethree = '4' or bonustypefour = '4' or bonustypeone = '4' or bonustypetwo = '4' or bonustypethree = '4' or bonustypefour = '4')","");
                    break;
                case "maxManaCB" : filterQuery = filterQuery.replace(" and (stattypeone = '12' or stattypetwo = '12' or stattypethree = '12' or stattypefour = '12' or bonustypeone = '12' or bonustypetwo = '12' or bonustypethree = '12' or bonustypefour = '12')","");
                    break;
                case "manaRegenCB" : filterQuery = filterQuery.replace(" and (stattypeone = '13' or stattypetwo = '13' or stattypethree = '13' or stattypefour = '13' or bonustypeone = '13' or bonustypetwo = '13' or bonustypethree = '13' or bonustypefour = '13')","");
                    break;
                case "energyPenCB" : filterQuery = filterQuery.replace(" and (stattypeone = '9' or stattypetwo = '9' or stattypethree = '9' or stattypefour = '9' or bonustypeone = '9' or bonustypetwo = '9' or bonustypethree = '9' or bonustypefour = '9')","");
                    break;
                case "physicalPenCB" : filterQuery = filterQuery.replace(" and (stattypeone = '7' or stattypetwo = '7' or stattypethree = '7' or stattypefour = '7' or bonustypeone = '7' or bonustypetwo = '7' or bonustypethree = '7' or bonustypefour = '7')","");
                    break;
                case "attackSpeedCB" : filterQuery = filterQuery.replace(" and (stattypeone = '2' or stattypetwo = '2' or stattypethree = '2' or stattypefour = '2' or bonustypeone = '2' or bonustypetwo = '2' or bonustypethree = '2' or bonustypefour = '2')","");
                    break;
                case "lifestealCB" : filterQuery = filterQuery.replace(" and (stattypeone = '5' or stattypetwo = '5' or stattypethree = '5' or stattypefour = '5' or bonustypeone = '5' or bonustypetwo = '5' or bonustypethree = '5' or bonustypefour = '5')","");
                    break;
                case "cooldownCB" : filterQuery = filterQuery.replace(" and (stattypeone = '14' or stattypetwo = '14' or stattypethree = '14' or stattypefour = '14' or bonustypeone = '14' or bonustypetwo = '14' or bonustypethree = '14' or bonustypefour = '14')","");
                    break;
            }
        }

        cardQuery = queryStart + filterQuery + queryEnd;

        Cursor filteredQuery = DataBaseHelper.FindFilteredCards(cardQuery);
        cardAdapter.swapCursor(filteredQuery);
    }
}

