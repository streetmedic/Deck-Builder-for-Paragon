package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HeroListAdapter extends CursorAdapter {
    public HeroListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.hero_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        RelativeLayout heroLayout = (RelativeLayout)view.findViewById(R.id.heroLayout);
        ImageView heroImage = (ImageView)view.findViewById(R.id.heroImage);
        TextView heroName = (TextView)view.findViewById(R.id.heroName);
        TextView heroRole = (TextView)view.findViewById(R.id.heroRole);

        int heroPic = context.getResources().getIdentifier(cursor.getString(1).toLowerCase().replace(" ","").replace(".","") + "_icon", "drawable", context.getPackageName());
        Picasso.with(context).load(heroPic).placeholder(R.drawable.placeholder).into(heroImage);

        heroLayout.setTag(cursor.getString(1));
        heroName.setText(cursor.getString(1).toUpperCase());
        heroRole.setText(cursor.getString(2).toUpperCase() + " : " + cursor.getString(3).replace("Affinity.","").toUpperCase() + " / " + cursor.getString(4).replace("Affinity.","").toUpperCase());

        final View.OnClickListener HeroSelect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrimeSelection.class);
                String hero = v.getTag().toString();
                Cursor heroAffs = DataBaseHelper.CheckAffinities(hero);
                if (heroAffs !=null && heroAffs.moveToFirst()){
                    String affOne = heroAffs.getString(0);
                    String affTwo = heroAffs.getString(1);
                    intent.putExtra("AFF_ONE", affOne);
                    intent.putExtra("AFF_TWO", affTwo);
                    intent.putExtra("Hero", hero);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        };

        heroLayout.setOnClickListener(HeroSelect);
    }
}