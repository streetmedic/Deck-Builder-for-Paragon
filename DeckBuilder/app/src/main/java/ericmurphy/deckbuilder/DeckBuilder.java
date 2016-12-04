package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Eric Murphy on 5/1/2016.
 */

public class DeckBuilder extends Activity {

    static ArrayList<String> statsArray = new ArrayList<>();

    public static String affOne, affTwo, heroName, pickedCard, upgradeCard, PrimeCard, savedDeckName;
    int upgradePos;

    Float energyDamageI;
    Float energyArmorI;
    Float energyPenI;
    Float physicalDamageI;
    Float physicalArmorI;
    Float physicalPenI;
    Float critChanceI;
    Float critDamageI;
    Float attackSpeedI;
    Float maxHPI;
    Float maxManaI;
    Float lifestealI;
    Float hpRegenI;
    Float manaRegenI;
    Float cooldownReductionI;

    Integer cardCount = 1;
    Integer equippedCards = 0;
    Integer activeCards = 0;

    String equipped, fromAct;

    CardListAdapter listAdapter = null;
    static ListView listView = null;
    static int index;
    static int top;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_builder);

        Intent intent = getIntent();
        affOne = intent.getStringExtra("AFF_ONE");
        affTwo = intent.getStringExtra("AFF_TWO");
        heroName = intent.getStringExtra("Hero");
        PrimeCard = intent.getStringExtra("PrimeCard");
        fromAct = intent.getStringExtra("FromActivity");
        savedDeckName = intent.getStringExtra("DeckName");
        if (fromAct.equals("ViewSavedDeck")) {
            statsArray = intent.getStringArrayListExtra("StatsArray");
        }

        Log.d("FROM ACT", fromAct);

        EditText deckName = (EditText) findViewById(R.id.deckName);

        if (savedDeckName != null) {
            deckName.setText(savedDeckName);
        } else {
            deckName.setText("New " + heroName);
        }

        UpdateStats();

        listView = (ListView) findViewById(R.id.equippedCardsList);
        listAdapter = new CardListAdapter(this, statsArray);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        pickedCard = intent.getStringExtra("DatabaseName");
        upgradeCard = intent.getStringExtra("UpgradeStats");
        upgradePos = intent.getIntExtra("UpgradePosition", 0);
        equipped = intent.getStringExtra("Equipped");
        if (intent.getStringExtra("FromActivity") != null) {
            fromAct = intent.getStringExtra("FromActivity");
        }
        Log.d("FROM ACT", fromAct);

        if (pickedCard != null) {
            Cursor cardStats = DataBaseHelper.LoadCardStats(pickedCard);
            if (cardStats.moveToFirst()) {
                statsArray.add(cardStats.getString(0) + "," +
                        cardStats.getString(1) + "," +
                        cardStats.getString(2) + "," +
                        cardStats.getString(3) + "," +
                        cardStats.getString(4) + "," +
                        cardStats.getString(5) + "," +
                        cardStats.getString(6) + "," +
                        cardStats.getString(7) + "," +
                        cardStats.getString(8) + "," +
                        cardStats.getString(9) + "," +
                        cardStats.getString(10) + "," +
                        cardStats.getString(11) + "," +
                        cardStats.getString(12) + "," +
                        cardStats.getString(13) + "," +
                        cardStats.getString(14) + "," +
                        cardStats.getString(15) + "," +
                        cardStats.getString(16) + "," +
                        cardStats.getString(17) + "," +
                        cardStats.getString(18) + "," +
                        cardStats.getString(19) + "," +
                        cardStats.getString(20) + "," +
                        "UpgradeOneName,UpgradeOneValue,UpgradeOneCost,UpgradeOneType,UpgradeTwoName,UpgradeTwoValue,UpgradeTwoCost,UpgradeTwoType,UpgradeThreeName,UpgradeThreeValue,UpgradeThreeCost,UpgradeThreeType," +
                        equipped
                );
            }
        }

        if (upgradeCard != null) {
            statsArray.set(upgradePos, upgradeCard);
        }

        UpdateStats();

        listView = (ListView)findViewById(R.id.equippedCardsList);
        listAdapter = new CardListAdapter(this, statsArray);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelectionFromTop(index, top);
            }
        });

        pickedCard = null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete Card");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Delete Card")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = Integer.parseInt(info.targetView.getTag().toString().replace("row",""));
            String[] split = statsArray.get(position).split(",");
            String card = split[0];
            Toast.makeText(DeckBuilder.this, card + " has been deleted", Toast.LENGTH_SHORT).show();
            statsArray.remove(position);
            listAdapter.notifyDataSetChanged();
            UpdateStats();
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme);
        builder.setTitle("Cancel Build");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                statsArray.clear();
//                Log.d("FROM ACT", fromAct);
//                if (fromAct.equals("PrimeSelection")) {
//                    Intent intent = new Intent(getApplicationContext(), HeroSelect.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
//                } else {
                    DeckBuilder.super.finish();
//                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void AddCard(View view) {
        Intent intent = new Intent(this, AddEquipmentCard.class);
        intent.putExtra("AffinityOne", affOne);
        intent.putExtra("AffinityTwo", affTwo);
        intent.putExtra("ButtonPress", view.getTag().toString());
        startActivity(intent);
    }

    public void UpdateStats() {
        energyDamageI = 0f;
        energyArmorI = 0f;
        energyPenI = 0f;
        physicalDamageI = 0f;
        physicalArmorI = 0f;
        physicalPenI = 0f;
        critChanceI = 0f;
        critDamageI = 0f;
        attackSpeedI = 0f;
        maxHPI = 0f;
        maxManaI = 0f;
        lifestealI = 0f;
        hpRegenI = 0f;
        manaRegenI = 0f;
        cooldownReductionI = 0f;

        cardCount = 1;
        activeCards = 0;
        equippedCards = 0;

        TextView energyDamage = (TextView) this.findViewById(R.id.Energy_Damage);
        TextView energyArmor = (TextView) this.findViewById(R.id.Energy_Armor);
        TextView energyPen = (TextView) this.findViewById(R.id.Energy_Pen);
        TextView physicalDamage = (TextView) this.findViewById(R.id.Physical_Damage);
        TextView physicalArmor = (TextView) this.findViewById(R.id.Physical_Armor);
        TextView physicalPen = (TextView) this.findViewById(R.id.Physical_Pen);
        TextView critChance = (TextView) this.findViewById(R.id.Crit_Chance);
        TextView critDamage = (TextView) this.findViewById(R.id.Crit_Damage);
        TextView attackSpeed = (TextView) this.findViewById(R.id.Attack_Speed);
        TextView maxHP = (TextView) this.findViewById(R.id.Max_Health);
        TextView maxMana = (TextView) this.findViewById(R.id.Max_Mana);
        TextView lifesteal = (TextView) this.findViewById(R.id.Lifesteal);
        TextView hpRegen = (TextView) this.findViewById(R.id.Health_Regen);
        TextView manaRegen = (TextView) this.findViewById(R.id.Mana_Regen);
        TextView cooldownReduction = (TextView) this.findViewById(R.id.Cooldown_Reduction);

        TextView totalCards = (TextView)findViewById(R.id.cardCount);
        TextView cardsEquipped = (TextView)findViewById(R.id.cardsEquipped);
        TextView equipmentCards = (TextView)findViewById(R.id.equipmentCards);

        for (Integer n = 0; n < statsArray.size(); n++) {
            String[] splitStats = statsArray.get(n).split(",");

            cardCount = cardCount + 1;

            if (!splitStats[21].equals("UpgradeOneName")) {
                cardCount = cardCount + 1;
            }

            if (!splitStats[25].equals("UpgradeTwoName")) {
                cardCount = cardCount + 1;
            }

            if (!splitStats[29].equals("UpgradeThreeName")) {
                cardCount = cardCount + 1;;
            }

            if (splitStats[33].equals("Yes")) {
                equippedCards = equippedCards + Integer.parseInt(splitStats[2]);
                activeCards++;

                if (!splitStats[3].equals("null")) {
                    FindType(splitStats[3], Float.parseFloat(splitStats[4].replace("%","")));
                }

                if (!splitStats[5].equals("null")) {
                    FindType(splitStats[5], Float.parseFloat(splitStats[6].replace("%","")));
                }

                if (!splitStats[7].equals("null")) {
                    FindType(splitStats[7], Float.parseFloat(splitStats[8].replace("%","")));
                }

                if (!splitStats[9].equals("null")) {
                    FindType(splitStats[9], Float.parseFloat(splitStats[10].replace("%","")));
                }

                if (splitStats[21].equals("UpgradeOneName")) {
                    splitStats[22] = "0";
                } else {
                    equippedCards = equippedCards + Integer.parseInt(splitStats[23]);
                }

                if (splitStats[25].equals("UpgradeTwoName")) {
                    splitStats[26] = "0";
                } else {
                    equippedCards = equippedCards + Integer.parseInt(splitStats[27]);
                }

                if (splitStats[29].equals("UpgradeThreeName")) {
                    splitStats[30] = "0";
                } else {
                    equippedCards = equippedCards + Integer.parseInt(splitStats[31]);
                }

                FindType(splitStats[24], Float.parseFloat(splitStats[22].replace("%","")));
                FindType(splitStats[28], Float.parseFloat(splitStats[26].replace("%","")));
                FindType(splitStats[32], Float.parseFloat(splitStats[30].replace("%","")));

                if (!splitStats[21].equals("UpgradeOneName") && !splitStats[25].equals("UpgradeTwoName") && !splitStats[29].equals("UpgradeThreeName")) {
                    if (!splitStats[11].equals("null")) {
                        FindType(splitStats[11], Float.parseFloat(splitStats[12].replace("%","")));
                    }

                    if (!splitStats[13].equals("null")) {
                        FindType(splitStats[13], Float.parseFloat(splitStats[14].replace("%","")));
                    }

                    if (!splitStats[15].equals("null")) {
                        FindType(splitStats[15], Float.parseFloat(splitStats[16].replace("%","")));
                    }

                    if (!splitStats[17].equals("null")) {
                        FindType(splitStats[17], Float.parseFloat(splitStats[18].replace("%","")));
                    }
                }
            }
        }

        energyDamage.setText(String.format("%d", Math.round(energyDamageI)));
        energyArmor.setText(String.format("%d", Math.round(energyArmorI)));
        energyPen.setText(String.format("%d", Math.round(energyPenI)));
        physicalDamage.setText(String.format("%d", Math.round(physicalDamageI)));
        physicalArmor.setText(String.format("%d", Math.round(physicalArmorI)));
        physicalPen.setText(String.format("%d", Math.round(physicalPenI)));
        critChance.setText(String.format("%d", Math.round(critChanceI)));
        critDamage.setText(String.format("%d", Math.round(critDamageI)));
        attackSpeed.setText(String.format("%d", Math.round(attackSpeedI)));
        maxHP.setText(String.format("%d", Math.round(maxHPI)));
        maxMana.setText(String.format("%d", Math.round(maxManaI)));
        lifesteal.setText(String.format("%d", Math.round(lifestealI)));
        hpRegen.setText(String.format("%d", Math.round(hpRegenI)));
        manaRegen.setText(String.format("%d", Math.round(manaRegenI)));
        cooldownReduction.setText(String.format("%d", Math.round(cooldownReductionI)));

        cardsEquipped.setText(equippedCards.toString() + "/60");
        equipmentCards.setText(activeCards.toString() + "/6");
        totalCards.setText(cardCount.toString() + "/40");

        if(equippedCards > 60) {
            cardsEquipped.setTextColor(Color.parseColor("#FFB20000"));
        } else {
            cardsEquipped.setTextColor(Color.parseColor("#eb9f0e"));
        }

        if(activeCards > 6) {
            equipmentCards.setTextColor(Color.parseColor("#FFB20000"));
        } else {
            equipmentCards.setTextColor(Color.parseColor("#eb9f0e"));
        }

        if(cardCount > 40) {
            totalCards.setTextColor(Color.parseColor("#FFB20000"));
        } else {
            totalCards.setTextColor(Color.parseColor("#eb9f0e"));
        }
    }

    public void FindType(String stat, Float value) {
        if (stat.equals("0")) {
            energyDamageI = energyDamageI + value;
        } else if (stat.equals("1")) {
            physicalDamageI = physicalDamageI + value;
        } else if (stat.equals("2")) {
            attackSpeedI = attackSpeedI + value;
        } else if (stat.equals("3")) {
            critChanceI = critChanceI + value;
        } else if (stat.equals("4")) {
            critDamageI = 50f;
        } else if (stat.equals("5")) {
            lifestealI = lifestealI + value;
        } else if (stat.equals("6")) {
            physicalArmorI = physicalArmorI + value;
        } else if (stat.equals("7")) {
            physicalPenI = physicalPenI + value;
        } else if (stat.equals("8")) {
            energyArmorI = energyArmorI + value;
        } else if (stat.equals("9")) {
            energyPenI = energyPenI + value;
        } else if (stat.equals("10")) {
            maxHPI = maxHPI + value;
        } else if (stat.equals("11")) {
            hpRegenI = hpRegenI + value;
        } else if (stat.equals("12")) {
            maxManaI = maxManaI + value;
        } else if (stat.equals("13")) {
            manaRegenI = manaRegenI + value;
        } else if (stat.equals("14")) {
            cooldownReductionI = cooldownReductionI + value;
        }
    }

    public void EquipCard(View view) {
        Integer position = Integer.parseInt(view.getTag().toString());
        String[] splitToEquip = statsArray.get(position).split(",");

        CheckBox checkBox = (CheckBox)view.findViewWithTag(view.getTag());
        if (checkBox.isChecked()) {
            splitToEquip[33] = "Yes";
        } else {
            splitToEquip[33] = "No";
        }
        String recombined = TextUtils.join(",", splitToEquip);
        statsArray.set(position, recombined);
        UpdateStats();
    }

    public void SaveDeck(View view) {
        final EditText deckName = (EditText) findViewById(R.id.deckName);
        if (deckName.getText().toString().contains(",")) {
            Toast.makeText(DeckBuilder.this, "Commas are not allowed in the deck name. Please change the name before saving.", Toast.LENGTH_SHORT).show();
        } else {
            if (cardCount <= 40 && cardCount >= 2) {
                Boolean deckExists = DataBaseHelper.checkForDuplicateDeck(deckName.getText().toString());
                if (deckExists) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme);
                    builder.setTitle("Build Exists");
                    builder.setMessage("A deck with this name already exists. Do you wish to replace it?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String insertString = CreateInsertString(deckName.getText().toString());
                            DataBaseHelper.SaveDeckToDatabase(deckName.getText().toString(), insertString);

                            GoToSavedDeck(deckName.getText().toString());
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    String insertString = CreateInsertString(deckName.getText().toString());
                    DataBaseHelper.SaveDeckToDatabase(deckName.getText().toString(), insertString);

                    GoToSavedDeck(deckName.getText().toString());
                }
            } else if (cardCount == 1) {
                Toast.makeText(DeckBuilder.this, "Add cards to your deck before saving!", Toast.LENGTH_SHORT).show();
            } else if (cardCount > 40) {
                Toast.makeText(DeckBuilder.this, "You have too many cards in your deck!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void GoToSavedDeck(String deckName) {
        Intent intent = new Intent(this, ViewSavedDeck.class);
        intent.putStringArrayListExtra("DeckArray", statsArray);
        intent.putExtra("DeckName", deckName);
        intent.putExtra("FromActivity", "DeckBuilder");
        intent.putExtra("PrimeCard", PrimeCard);
        intent.putExtra("HeroName", heroName);
        startActivity(intent);
        this.finish();
    }

    public String CreateInsertString(String deckName) {
        String combiningStats = "'" + deckName + "','" + heroName + "','" + PrimeCard + "'";
        for (Integer n = 0; n < statsArray.size(); n++) {
            String[] splitStats = statsArray.get(n).split(",");
            Log.d("ARRAY STATS", statsArray.toString());

            combiningStats = combiningStats + ",'" + splitStats[0].replace("'","''") + "'";

            if (!splitStats[21].equals("UpgradeOneName")) {
                combiningStats = combiningStats + ",'" + splitStats[21].replace("'","''") + "'";
            } else {
                combiningStats = combiningStats + "," + "null";
            }

            if (!splitStats[25].equals("UpgradeTwoName")) {
                combiningStats = combiningStats + ",'" + splitStats[25].replace("'","''") + "'";
            } else {
                combiningStats = combiningStats + "," + "null";
            }

            if (!splitStats[29].equals("UpgradeThreeName")) {
                combiningStats = combiningStats + ",'" + splitStats[29].replace("'","''") + "'";
            } else {
                combiningStats = combiningStats + "," + "null";
            }
        }
        Log.d("STATS FOR DB", combiningStats);

        String[] s = combiningStats.split(",");
        Integer length = s.length;
        Integer total = 159;
        Integer n = 0;

        while (n < (total - length)) {
            combiningStats = combiningStats + "," + "null";
            n++;
        }
        return combiningStats;
    }

    public static void SaveScrollState() {
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
    }

    public void LoadDetailedStats(View v) {
        Intent intent = new Intent(v.getContext(), DetailedStats.class);
        intent.putExtra("energyDamageI",energyDamageI);
        intent.putExtra("energyArmorI",energyArmorI);
        intent.putExtra("energyPenI",energyPenI);
        intent.putExtra("physicalDamageI",physicalDamageI);
        intent.putExtra("physicalArmorI",physicalArmorI);
        intent.putExtra("physicalPenI",physicalPenI);
        intent.putExtra("critChanceI",critChanceI);
        intent.putExtra("critDamageI",critDamageI);
        intent.putExtra("attackSpeedI",attackSpeedI);
        intent.putExtra("maxHPI",maxHPI);
        intent.putExtra("maxManaI",maxManaI);
        intent.putExtra("lifestealI",lifestealI);
        intent.putExtra("hpRegenI",hpRegenI);
        intent.putExtra("manaRegenI",manaRegenI);
        intent.putExtra("cooldownReductionI",cooldownReductionI);
        intent.putExtra("HeroName", heroName);
        startActivity(intent);
    }

}