package ericmurphy.deckbuilder;

import android.content.Context;
import android.content.Intent;
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

public class SavedListAdapter extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    public final ArrayList<String> cards;
    public static String equipped;

    public SavedListAdapter(Context c, ArrayList<String> cards) {
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
            row = inflater.inflate(R.layout.saved_deck_list, parent, false);
        } else {
            row = convertView;
        }

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

        if (cardsSplit[19].equals("Yes")) {
            row.findViewById(R.id.upgradeOneBox).setVisibility(View.VISIBLE);
            row.findViewById(R.id.upgradeTwoBox).setVisibility(View.VISIBLE);
            row.findViewById(R.id.upgradeThreeBox).setVisibility(View.VISIBLE);

            //Upgrade one 20, 21, 22, 23
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

            //Upgrade one 24, 25, 26, 27
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

            //Upgrade one 28, 29, 30, 31
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

        } else {
            row.findViewById(R.id.upgradeOneBox).setVisibility(View.GONE);
            row.findViewById(R.id.upgradeTwoBox).setVisibility(View.GONE);
            row.findViewById(R.id.upgradeThreeBox).setVisibility(View.GONE);
        }

        return row;
    }

    public String GetStatType(String cardStat) {
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
            case "4" : statType = "icon_critbonus";
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
