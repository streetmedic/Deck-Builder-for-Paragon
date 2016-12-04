package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by murphyfa on 5/26/2016.
 */
public class PrimeSelection extends Activity {

    String affOne, affTwo, hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prime_selection);

        Intent intent = getIntent();
        affOne = intent.getStringExtra("AFF_ONE");
        affTwo = intent.getStringExtra("AFF_TWO");
        hero = intent.getStringExtra("Hero");
    }

    public void PrimeSelected(View view) {
        String primeSelection = view.getTag().toString().replace("_"," ");
        Intent intent = new Intent(this, DeckBuilder.class);
        intent.putExtra("PrimeCard", primeSelection);
        intent.putExtra("AFF_ONE", affOne);
        intent.putExtra("AFF_TWO", affTwo);
        intent.putExtra("Hero", hero);
        intent.putExtra("FromActivity", "PrimeSelection");
        startActivity(intent);
        finish();
    }
}
