package ericmurphy.deckbuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardListAdapter extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    public final ArrayList<String> cards;

    public CardListAdapter(Context c, ArrayList<String> cards) {
        super (c, R.layout.grid_card, cards);
        this.mContext = c;
        this.cards = cards;


        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final String[] cardsSplit = cards.get(position).split(",");

        if (convertView == null) {
            row = inflater.inflate(R.layout.equipment_in_list, parent, false);
        } else {
            row = convertView;
        }

        //Long click is for deleting cards
        row.setLongClickable(true);
        row.setTag("row" + position);

        //Card image in list item
        ImageView cardImage = (ImageView) row.findViewById(R.id.cardImage);
        String cardName = "card_" + cardsSplit[0].replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase();
        int cardPic = mContext.getResources().getIdentifier(cardName, "drawable", mContext.getPackageName());
        if (cardPic != 0) {
            Picasso.with(mContext).load(cardPic).placeholder(R.drawable.placeholder).into(cardImage);
        } else {
            Picasso.with(mContext).load(R.drawable.placeholder).into(cardImage);
        }

        TextView cardNameText = (TextView) row.findViewById(R.id.cardName);
        cardNameText.setText(cardsSplit[0]);

        TextView cardCostText = (TextView) row.findViewById(R.id.cardCost);
        cardCostText.setText("Cost: " + cardsSplit[2]);

        ImageView statOneIcon = (ImageView) row.findViewById(R.id.statOneIcon);
        ImageView statTwoIcon = (ImageView) row.findViewById(R.id.statTwoIcon);
        ImageView statThreeIcon = (ImageView) row.findViewById(R.id.statThreeIcon);
        ImageView statFourIcon = (ImageView) row.findViewById(R.id.statFourIcon);

        if (!cardsSplit[3].equals("null")) {
            statOneIcon.setVisibility(View.VISIBLE);
            statOneIcon.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[3]), "drawable", mContext.getPackageName()));
        } else {
            statOneIcon.setVisibility(View.GONE);
        }

        if (!cardsSplit[5].equals("null")) {
            statTwoIcon.setVisibility(View.VISIBLE);
            statTwoIcon.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[5]), "drawable", mContext.getPackageName()));
        } else {
            statTwoIcon.setVisibility(View.GONE);
        }

        if (!cardsSplit[7].equals("null")) {
            statThreeIcon.setVisibility(View.VISIBLE);
            statThreeIcon.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[7]), "drawable", mContext.getPackageName()));
        } else {
            statThreeIcon.setVisibility(View.GONE);
        }

        if (!cardsSplit[9].equals("null")) {
            statFourIcon.setVisibility(View.VISIBLE);
            statFourIcon.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[9]), "drawable", mContext.getPackageName()));
        } else {
            statFourIcon.setVisibility(View.GONE);
        }

        TextView statOneText = (TextView) row.findViewById(R.id.statOneNum);
        TextView statTwoText = (TextView) row.findViewById(R.id.statTwoNum);
        TextView statThreeText = (TextView) row.findViewById(R.id.statThreeNum);
        TextView statFourText = (TextView) row.findViewById(R.id.statFourNum);

        if (!cardsSplit[4].equals("null")) {
            statOneText.setVisibility(View.VISIBLE);
            statOneText.setText(cardsSplit[4]);
        } else statOneText.setVisibility(View.GONE);

        if (!cardsSplit[6].equals("null")) {
            statTwoText.setVisibility(View.VISIBLE);
            statTwoText.setText(cardsSplit[6]);
        } else statTwoText.setVisibility(View.GONE);

        if (!cardsSplit[8].equals("null")) {
            statThreeText.setVisibility(View.VISIBLE);
            statThreeText.setText(cardsSplit[8]);
        } else statThreeText.setVisibility(View.GONE);

        if (!cardsSplit[10].equals("null")) {
            statFourText.setVisibility(View.VISIBLE);
            statFourText.setText(cardsSplit[10]);
        } else statFourText.setVisibility(View.GONE);

        //Listener for adding upgrades to cards
        View.OnClickListener UpgradeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddUpgradeCard.class);
                intent.putExtra("StatsArray", cards.get(position));
                intent.putExtra("Position", (Integer)position);
                intent.putExtra("ButtonTag", v.getTag().toString());
                intent.putExtra("TypeOne", cardsSplit[3]);
                intent.putExtra("TypeTwo", cardsSplit[5]);
                intent.putExtra("TypeThree", cardsSplit[7]);
                intent.putExtra("TypeFour", cardsSplit[9]);
                intent.putExtra("AffOne", DeckBuilder.affOne);
                intent.putExtra("AffTwo", DeckBuilder.affTwo);
                DeckBuilder.SaveScrollState();
                mContext.startActivity(intent);
            }
        };

        if (cardsSplit[19].equals("Yes")) {
            row.findViewById(R.id.upgradeOneBox).setVisibility(View.VISIBLE);
            row.findViewById(R.id.upgradeTwoBox).setVisibility(View.VISIBLE);
            row.findViewById(R.id.upgradeThreeBox).setVisibility(View.VISIBLE);
            row.findViewById(R.id.equippedCB).setVisibility(View.VISIBLE);
            row.findViewById(R.id.equippedText).setVisibility(View.VISIBLE);

            RelativeLayout upgradeOneRL = (RelativeLayout)row.findViewById(R.id.upgradeOneBox);
            RelativeLayout upgradeTwoRL = (RelativeLayout)row.findViewById(R.id.upgradeTwoBox);
            RelativeLayout upgradeThreeRL = (RelativeLayout)row.findViewById(R.id.upgradeThreeBox);
            upgradeOneRL.setOnClickListener(UpgradeListener);
            upgradeTwoRL.setOnClickListener(UpgradeListener);
            upgradeThreeRL.setOnClickListener(UpgradeListener);

            //Upgrade one 21,22,23,24
            //Name, Value, Cost, Type
            TextView upgradeOneCostTV = (TextView)row.findViewById(R.id.upgradeOneCost);
            ImageView upgradeOneImageIV = (ImageView)row.findViewById(R.id.upgradeOneImage);
            TextView upgradeOneValueTV = (TextView)row.findViewById(R.id.upgradeOneValue);
            if (!cardsSplit[21].equals("UpgradeOneName")) {
                upgradeOneCostTV.setVisibility(View.VISIBLE);
                upgradeOneValueTV.setVisibility(View.VISIBLE);
                upgradeOneCostTV.setText(cardsSplit[23] + "CP");
                upgradeOneValueTV.setText(cardsSplit[22]);
                upgradeOneImageIV.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[24].replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", mContext.getPackageName()));
            } else {
                upgradeOneImageIV.setImageResource(R.drawable.plus_sign);
                upgradeOneCostTV.setVisibility(View.INVISIBLE);
                upgradeOneValueTV.setVisibility(View.INVISIBLE);
            }

            //Upgrade two 25,26,27,28
            //Name, Value, Cost, Type
            TextView upgradeTwoCostTV = (TextView)row.findViewById(R.id.upgradeTwoCost);
            ImageView upgradeTwoImageIV = (ImageView)row.findViewById(R.id.upgradeTwoImage);
            TextView upgradeTwoValueTV = (TextView)row.findViewById(R.id.upgradeTwoValue);
            if (!cardsSplit[25].equals("UpgradeTwoName")) {
                upgradeTwoCostTV.setVisibility(View.VISIBLE);
                upgradeTwoValueTV.setVisibility(View.VISIBLE);
                upgradeTwoCostTV.setText(cardsSplit[27] + "CP");
                upgradeTwoValueTV.setText(cardsSplit[26]);
                upgradeTwoImageIV.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[28].replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", mContext.getPackageName()));
            } else {
                upgradeTwoImageIV.setImageResource(R.drawable.plus_sign);
                upgradeTwoCostTV.setVisibility(View.GONE);
                upgradeTwoValueTV.setVisibility(View.GONE);
            }

            //Upgrade three 29,30,31,32
            //Name, Value, Cost, Type
            TextView upgradeThreeCostTV = (TextView)row.findViewById(R.id.upgradeThreeCost);
            ImageView upgradeThreeImageIV = (ImageView)row.findViewById(R.id.upgradeThreeImage);
            TextView upgradeThreeValueTV = (TextView)row.findViewById(R.id.upgradeThreeValue);
            if (!cardsSplit[29].equals("UpgradeThreeName")) {
                upgradeThreeCostTV.setVisibility(View.VISIBLE);
                upgradeThreeValueTV.setVisibility(View.VISIBLE);
                upgradeThreeCostTV.setText(cardsSplit[31] + "CP");
                upgradeThreeValueTV.setText(cardsSplit[30]);
                upgradeThreeImageIV.setImageResource(mContext.getResources().getIdentifier(GetStatType(cardsSplit[32].replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").toLowerCase()), "drawable", mContext.getPackageName()));
            } else {
                upgradeThreeImageIV.setImageResource(R.drawable.plus_sign);
                upgradeThreeCostTV.setVisibility(View.GONE);
                upgradeThreeValueTV.setVisibility(View.GONE);
            }

            //Update Equipment Card's stats to reflect bonus included
            if (!cardsSplit[21].equals("UpgradeOneName") && !cardsSplit[25].equals("UpgradeTwoName") && !cardsSplit[29].equals("UpgradeThreeName")) {

                if (!GetStatType(cardsSplit[3]).equals("null") && GetStatType(cardsSplit[3]).equals(GetStatType(cardsSplit[11]))) {
                    statOneText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[4]) + Float.parseFloat(cardsSplit[12]))));
                    statOneText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[3]).equals("null") && GetStatType(cardsSplit[3]).equals(GetStatType(cardsSplit[13]))) {
                    statOneText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[4]) + Float.parseFloat(cardsSplit[14]))));
                    statOneText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[3]).equals("null") && GetStatType(cardsSplit[3]).equals(GetStatType(cardsSplit[15]))) {
                    statOneText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[4]) + Float.parseFloat(cardsSplit[16]))));
                    statOneText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[3]).equals("null") && GetStatType(cardsSplit[3]).equals(GetStatType(cardsSplit[17]))) {
                    statOneText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[4]) + Float.parseFloat(cardsSplit[18]))));
                    statOneText.setTextColor(Color.parseColor("#eb9f0e"));
                }

                if (!GetStatType(cardsSplit[5]).equals("null") && GetStatType(cardsSplit[5]).equals(GetStatType(cardsSplit[11]))) {
                    statTwoText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[6]) + Float.parseFloat(cardsSplit[12]))));
                    statTwoText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[5]).equals("null") && GetStatType(cardsSplit[5]).equals(GetStatType(cardsSplit[13]))) {
                    statTwoText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[6]) + Float.parseFloat(cardsSplit[14]))));
                    statTwoText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[5]).equals("null") && GetStatType(cardsSplit[5]).equals(GetStatType(cardsSplit[15]))) {
                    statTwoText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[6]) + Float.parseFloat(cardsSplit[16]))));
                    statTwoText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[5]).equals("null") && GetStatType(cardsSplit[5]).equals(GetStatType(cardsSplit[17]))) {
                    statTwoText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[6]) + Float.parseFloat(cardsSplit[18]))));
                    statTwoText.setTextColor(Color.parseColor("#eb9f0e"));
                }

                if (!GetStatType(cardsSplit[7]).equals("null") && GetStatType(cardsSplit[7]).equals(GetStatType(cardsSplit[11]))) {
                    statThreeText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[8]) + Float.parseFloat(cardsSplit[12]))));
                    statThreeText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[7]).equals("null") && GetStatType(cardsSplit[7]).equals(GetStatType(cardsSplit[13]))) {
                    statThreeText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[8]) + Float.parseFloat(cardsSplit[14]))));
                    statThreeText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[7]).equals("null") && GetStatType(cardsSplit[7]).equals(GetStatType(cardsSplit[15]))) {
                    statThreeText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[8]) + Float.parseFloat(cardsSplit[16]))));
                    statThreeText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[7]).equals("null") && GetStatType(cardsSplit[7]).equals(GetStatType(cardsSplit[17]))) {
                    statThreeText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[8]) + Float.parseFloat(cardsSplit[18]))));
                    statThreeText.setTextColor(Color.parseColor("#eb9f0e"));
                }

                if (!GetStatType(cardsSplit[9]).equals("null") && GetStatType(cardsSplit[9]).equals(GetStatType(cardsSplit[11]))) {
                    statFourText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[10]) + Float.parseFloat(cardsSplit[12]))));
                    statFourText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[9]).equals("null") && GetStatType(cardsSplit[9]).equals(GetStatType(cardsSplit[13]))) {
                    statFourText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[10]) + Float.parseFloat(cardsSplit[14]))));
                    statFourText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[9]).equals("null") && GetStatType(cardsSplit[9]).equals(GetStatType(cardsSplit[15]))) {
                    statFourText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[10]) + Float.parseFloat(cardsSplit[16]))));
                    statFourText.setTextColor(Color.parseColor("#eb9f0e"));
                } else if (!GetStatType(cardsSplit[9]).equals("null") && GetStatType(cardsSplit[9]).equals(GetStatType(cardsSplit[17]))) {
                    statFourText.setText(String.format("%d", Math.round(Float.parseFloat(cardsSplit[10]) + Float.parseFloat(cardsSplit[18]))));
                    statFourText.setTextColor(Color.parseColor("#eb9f0e"));
                }
            }
            //if it cannot be upgraded, hide the buttons
        } else {
            row.findViewById(R.id.upgradeOneBox).setVisibility(View.GONE);
            row.findViewById(R.id.upgradeTwoBox).setVisibility(View.GONE);
            row.findViewById(R.id.upgradeThreeBox).setVisibility(View.GONE);
        }

        //Checkbox to equip it
        CheckBox equippedCB = (CheckBox)row.findViewById(R.id.equippedCB);
        if (cardsSplit[20].equals("Yes")) {
            equippedCB.setVisibility(View.VISIBLE);
            row.findViewById(R.id.equippedText).setVisibility(View.VISIBLE);
            equippedCB.setTag(position);
            if (cardsSplit[33].equals("Yes")) {
                equippedCB.setChecked(true);
            } else {
                equippedCB.setChecked(false);
            }
        } else {
            equippedCB.setVisibility(View.GONE);
            row.findViewById(R.id.equippedText).setVisibility(View.GONE);
        }

        return row;
    }

    public static String GetStatType(String cardStat) {
        String statType = cardStat;
        switch (statType) {
            case "0" : statType = "icon_energydamage";
                break;
            case "1" : statType = "icon_physicaldamage";
                break;
            case "2" : statType = "icon_attackspeed";
                break;
            case "3" : statType = "icon_critchance";
                break;
            case "4" : statType = "icon_critdamage";
                break;
            case "5" : statType = "icon_lifesteal";
                break;
            case "6" : statType = "icon_physicalarmor";
                break;
            case "7" : statType = "icon_physicalpen";
                break;
            case "8" : statType = "icon_energyarmor";
                break;
            case "9" : statType = "icon_energypen";
                break;
            case "10" : statType = "icon_maxhealth";
                break;
            case "11" : statType = "icon_healthregen";
                break;
            case "12" : statType = "icon_maxmana";
                break;
            case "13" : statType = "icon_manaregen";
                break;
            case "14" : statType = "icon_cooldownreduction";
                break;
            case "15" : statType = "icon_movementspeed";
                break;
            case "16" : statType = "icon_damagebonus";
                break;
            case "17" : statType = "icon_harvestertime";
                break;
        }
        return statType;
    }
}
