package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by murphyfa on 5/5/2016.
 */
public class EquipmentDetails extends Activity {

    public String cardName, dbCardName, ViewOnly;
    public static String equipped = "Yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_details);

        Intent intent = getIntent();

        cardName = intent.getStringExtra("CardName");
        dbCardName = intent.getStringExtra("DatabaseName");
        ViewOnly = intent.getStringExtra("ViewOnly");

        TextView cardName = (TextView)findViewById(R.id.cardName);
        TextView cardType = (TextView)findViewById(R.id.cardType);
        TextView costLabel = (TextView)findViewById(R.id.costLabel);
        TextView rarityLabel = (TextView)findViewById(R.id.rarityLabel);
        TextView affinityLabel = (TextView)findViewById(R.id.affinityLabel);

        LinearLayout statOne = (LinearLayout)findViewById(R.id.statOne);
        TextView statOneValue = (TextView)findViewById(R.id.statOneValue);
        ImageView statOneIcon = (ImageView)findViewById(R.id.statOneIcon);
        TextView statOneType = (TextView)findViewById(R.id.statOneType);

        LinearLayout statTwo = (LinearLayout)findViewById(R.id.statTwo);
        TextView statTwoValue = (TextView)findViewById(R.id.statTwoValue);
        ImageView statTwoIcon = (ImageView)findViewById(R.id.statTwoIcon);
        TextView statTwoType = (TextView)findViewById(R.id.statTwoType);

        LinearLayout statThree = (LinearLayout)findViewById(R.id.statThree);
        TextView statThreeValue = (TextView)findViewById(R.id.statThreeValue);
        ImageView statThreeIcon = (ImageView)findViewById(R.id.statThreeIcon);
        TextView statThreeType = (TextView)findViewById(R.id.statThreeType);

        LinearLayout statFour = (LinearLayout)findViewById(R.id.statFour);
        TextView statFourValue = (TextView)findViewById(R.id.statFourValue);
        ImageView statFourIcon = (ImageView)findViewById(R.id.statFourIcon);
        TextView statFourType = (TextView)findViewById(R.id.statFourType);

        TextView specialLabel = (TextView)findViewById(R.id.specialEffect);

        LinearLayout bonusDivider = (LinearLayout) findViewById(R.id.bonusDivider);
        TextView upgradeBonusLabel = (TextView)findViewById(R.id.upgradeBonusLabel);

        LinearLayout bonusOne = (LinearLayout) findViewById(R.id.bonusOne);
        TextView bonusOneValue = (TextView)findViewById(R.id.bonusOneValue);
        ImageView bonusOneIcon = (ImageView)findViewById(R.id.bonusOneIcon);
        TextView bonusOneType = (TextView)findViewById(R.id.bonusOneType);

        LinearLayout bonusTwo = (LinearLayout) findViewById(R.id.bonusTwo);
        TextView bonusTwoValue = (TextView)findViewById(R.id.bonusTwoValue);
        ImageView bonusTwoIcon = (ImageView)findViewById(R.id.bonusTwoIcon);
        TextView bonusTwoType = (TextView)findViewById(R.id.bonusTwoType);

        Cursor cardStats = DataBaseHelper.LoadCardStatsForDetails(dbCardName);

        if (cardStats.moveToFirst()) {
            cardName.setText(cardStats.getString(0));
            cardType.setText(cardStats.getString(1));
            costLabel.setText("Cost: " + cardStats.getString(2));
            rarityLabel.setText(cardStats.getString(3));
            affinityLabel.setText(cardStats.getString(4).replace("Affinity.",""));

            if (cardStats.getString(5) != null) {
                statOne.setVisibility(View.VISIBLE);
                statOneValue.setText(cardStats.getString(6));
                statOneIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(5).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                statOneType.setText(GetType(cardStats.getString(5)));
            } else {
                statOne.setVisibility(View.GONE);
            }

            if (cardStats.getString(7) != null) {
                statTwo.setVisibility(View.VISIBLE);
                statTwoValue.setText(cardStats.getString(8));
                statTwoIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(7).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                statTwoType.setText(GetType(cardStats.getString(7)));
            } else {
                statTwo.setVisibility(View.GONE);
            }

            if (cardStats.getString(9) != null) {
                statThree.setVisibility(View.VISIBLE);
                statThreeValue.setText(cardStats.getString(10));
                statThreeIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(9).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                statThreeType.setText(GetType(cardStats.getString(9)));
            } else {
                statThree.setVisibility(View.GONE);
            }

            if (cardStats.getString(11) != null) {
                statFour.setVisibility(View.VISIBLE);
                statFourValue.setText(cardStats.getString(12));
                statFourIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(11).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                statFourType.setText(GetType(cardStats.getString(11)));
            } else {
                statFour.setVisibility(View.GONE);
            }

            if (cardStats.getString(23) != null) {
                specialLabel.setVisibility(View.VISIBLE);
                specialLabel.setText(cardStats.getString(23));
            } else {
                specialLabel.setVisibility(View.GONE);
            }

            if (cardStats.getString(13) != null) {
                bonusOne.setVisibility(View.VISIBLE);
                bonusDivider.setVisibility(View.VISIBLE);
                upgradeBonusLabel.setVisibility(View.VISIBLE);
                bonusOneValue.setText(cardStats.getString(14));
                bonusOneIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(13).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                bonusOneType.setText(GetType(cardStats.getString(13)));
            } else {
                bonusDivider.setVisibility(View.GONE);
                upgradeBonusLabel.setVisibility(View.GONE);
                bonusOne.setVisibility(View.GONE);
            }

            if (cardStats.getString(15) != null) {
                bonusTwo.setVisibility(View.VISIBLE);
                bonusTwoValue.setText(cardStats.getString(16));
                bonusTwoIcon.setImageResource(this.getResources().getIdentifier(CardListAdapter.GetStatType(cardStats.getString(15).replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", this.getPackageName()));
                bonusTwoType.setText(GetType(cardStats.getString(15)));
            } else {
                bonusTwo.setVisibility(View.GONE);
            }


        }

        CheckBox equippedCB = (CheckBox)findViewById(R.id.detailsEquippedCB);
        equippedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    equipped = "Yes";
                } else {
                    equipped = "No";
                }
            }
        });

        Button addButton = (Button)findViewById(R.id.addButton);

        if (ViewOnly != null) {
            addButton.setVisibility(View.GONE);
            equippedCB.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.VISIBLE);
            equippedCB.setVisibility(View.VISIBLE);
        }
    }

    public void SelectEquipmentCard(View view) {
        Intent intent = new Intent(this, DeckBuilder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("DatabaseName", dbCardName);
        intent.putExtra("Equipped", equipped);
        startActivity(intent);
    }

    public static String GetType(String cardStat) {
        String statType = cardStat;
        switch (statType) {
            case "0" : statType = "Energy Damage";
                break;
            case "1" : statType = "Physical Damage";
                break;
            case "2" : statType = "Attack Speed";
                break;
            case "3" : statType = "Crit Chance";
                break;
            case "4" : statType = "Crit Bonus [UNIQUE]";
                break;
            case "5" : statType = "Lifesteal";
                break;
            case "6" : statType = "Physical Armor";
                break;
            case "7" : statType = "Physical Pen";
                break;
            case "8" : statType = "Energy Armor";
                break;
            case "9" : statType = "Energy Pen";
                break;
            case "10" : statType = "Max Health";
                break;
            case "11" : statType = "Health Regen";
                break;
            case "12" : statType = "Max Mana";
                break;
            case "13" : statType = "Mana Regen";
                break;
            case "14" : statType = "Cooldown Reduction";
                break;
        }
        return statType;
    }
}
