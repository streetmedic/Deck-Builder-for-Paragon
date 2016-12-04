package ericmurphy.deckbuilder;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class HeroSelect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_select);

        Cursor heroList = DataBaseHelper.FindHeroes();

        ListView listView = (ListView)this.findViewById(R.id.heroList);
        HeroListAdapter heroListAdapter = new HeroListAdapter(this, heroList, 0);
        listView.setAdapter(heroListAdapter);
    }
}