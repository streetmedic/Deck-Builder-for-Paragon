package ericmurphy.deckbuilder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class DeckListAdapter extends CursorAdapter {
    public DeckListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.saved_decks_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        RelativeLayout deckLayout = (RelativeLayout)view.findViewById(R.id.deckListItem);
        ImageView heroImage = (ImageView)view.findViewById(R.id.heroImage);
        TextView deckName = (TextView)view.findViewById(R.id.deckName);

        int heroPic = context.getResources().getIdentifier(cursor.getString(2).toLowerCase().replace(" ","").replace(".","") + "_icon", "drawable", context.getPackageName());
        Picasso.with(context).load(heroPic).into(heroImage);

        deckName.setText(cursor.getString(1));

        deckLayout.setTag(cursor.getString(1));
        deckLayout.setLongClickable(true);
    }
}