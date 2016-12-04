package ericmurphy.deckbuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CardAdapter extends CursorAdapter {
    public CardAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_card, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        RelativeLayout cardLayout = (RelativeLayout)view.findViewById(R.id.cardLayout);

        ImageView cardImage = (ImageView)view.findViewById(R.id.cardImage);
        TextView cardName = (TextView)view.findViewById(R.id.cardName);

        LinearLayout statOne = (LinearLayout)view.findViewById(R.id.statOne);
        ImageView statOneIcon = (ImageView)view.findViewById(R.id.statOneIcon);
        TextView statOneValue = (TextView)view.findViewById(R.id.statOneValue);

        LinearLayout statTwo = (LinearLayout)view.findViewById(R.id.statTwo);
        ImageView statTwoIcon = (ImageView)view.findViewById(R.id.statTwoIcon);
        TextView statTwoValue = (TextView)view.findViewById(R.id.statTwoValue);

        LinearLayout statThree = (LinearLayout)view.findViewById(R.id.statThree);
        ImageView statThreeIcon = (ImageView)view.findViewById(R.id.statThreeIcon);
        TextView statThreeValue = (TextView)view.findViewById(R.id.statThreeValue);

        LinearLayout statFour = (LinearLayout)view.findViewById(R.id.statFour);
        ImageView statFourIcon = (ImageView)view.findViewById(R.id.statFourIcon);
        TextView statFourValue = (TextView)view.findViewById(R.id.statFourValue);

        TextView cardCost = (TextView)view.findViewById(R.id.cardCost);
        TextView type = (TextView)view.findViewById(R.id.type);
        TextView affinity = (TextView)view.findViewById(R.id.affinity);

        //Setting tag for easier reference
        cardLayout.setTag(cursor.getString(1));

        //This is only supported in API 21+ so don't use it for KitKat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Setting the background to match affinity color
            cardLayout.setBackground(context.getDrawable(SelectBackground(cursor.getString(4))));
            cardLayout.setPadding(0, 0, 0, 0);
        }

        int cardPic = context.getResources().getIdentifier("card_" + cursor.getString(1).toLowerCase().replace(" ","_").replace("'","").replace("-","_"), "drawable", context.getPackageName());
        if (cardPic != 0) {
            Picasso.with(context).load(cardPic).placeholder(R.drawable.placeholder).into(cardImage);
        } else {
            Picasso.with(context).load(R.drawable.placeholder).into(cardImage);
        }

        //Card Name
        cardName.setText(cursor.getString(1));

        //Card Cost
        cardCost.setText("CP: " + cursor.getString(3));

        //Card Type (Upgrade/Equipment)
        type.setText(cursor.getString(2));

        //Card Affinity
        affinity.setText(cursor.getString(4).replace("Affinity.",""));

        //Hiding stat icons and values if they do not exist for a particular card
        //Icon and value for stat one
        if (cursor.getString(5) != null) {
            statOne.setVisibility(View.VISIBLE);
            statOneIcon.setImageResource(context.getResources().getIdentifier(CardListAdapter.GetStatType(cursor.getString(5)), "drawable", context.getPackageName()));
            statOneValue.setText(cursor.getString(6));
        } else {
            statOne.setVisibility(View.GONE);
        }

        //Icon and value for stat two
        if (cursor.getString(7) != null) {
            statTwo.setVisibility(View.VISIBLE);
            statTwoIcon.setImageResource(context.getResources().getIdentifier(CardListAdapter.GetStatType(cursor.getString(7)), "drawable", context.getPackageName()));
            statTwoValue.setText(cursor.getString(8));
        } else {
            statTwo.setVisibility(View.GONE);
        }

        //Icon and value for stat three
        if (cursor.getString(9) != null) {
            statThree.setVisibility(View.VISIBLE);
            statThreeIcon.setImageResource(context.getResources().getIdentifier(CardListAdapter.GetStatType(cursor.getString(9)), "drawable", context.getPackageName()));
            statThreeValue.setText(cursor.getString(10));
        } else {
            statThree.setVisibility(View.GONE);
        }

        //Icon and value for stat four
        if (cursor.getString(11) != null) {
            statFour.setVisibility(View.VISIBLE);
            statFourIcon.setImageResource(context.getResources().getIdentifier(CardListAdapter.GetStatType(cursor.getString(11)), "drawable", context.getPackageName()));
            statFourValue.setText(cursor.getString(12));
        } else {
            statFour.setVisibility(View.GONE);
        }
    }

    public int SelectBackground(String affinity) {
        int affinityBG = R.drawable.dark_silver_frame;

        if (affinity.equals("Affinity.Corruption")) {
            affinityBG = R.drawable.corruption_frame;
        } else if (affinity.equals("Affinity.Fury")) {
            affinityBG = R.drawable.fury_frame;
        } else if (affinity.equals("Affinity.Growth")) {
            affinityBG = R.drawable.growth_frame;
        } else if (affinity.equals("Affinity.Intellect")) {
            affinityBG = R.drawable.intellect_frame;
        } else if (affinity.equals("Affinity.Order")) {
            affinityBG = R.drawable.order_frame;
        }

        return affinityBG;
    }
}