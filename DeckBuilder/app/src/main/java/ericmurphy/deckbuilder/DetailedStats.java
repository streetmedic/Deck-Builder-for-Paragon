package ericmurphy.deckbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Me on 6/9/2016.
 */
public class DetailedStats extends Activity {

    String heroNameString;

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

    //For Max HP/Mana BaseHP is for select Hero level + maxHPI from cards
    Float baseHP = 500f;
    Float maxHP;
    Float baseMana = 500f;
    Float maxMana;

    //For DPS calculations
    Float baseEDMG = 1f;
    Float basePDMG = 1f;
    Float baseAT = 1f;
    Float baseASR = 1f;
    Float critMulti = 200f;

    Float attackDamage;
    Float critDamage;
    Float attackSpeed;
    Float dps;
    Float critDPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_stats);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Getting equipped card stats
        Intent intent = getIntent();
        energyDamageI = intent.getFloatExtra("energyDamageI", 0);
        energyArmorI = intent.getFloatExtra("energyArmorI", 0);
        energyPenI = intent.getFloatExtra("energyPenI", 0);
        physicalDamageI = intent.getFloatExtra("physicalDamageI", 0);
        physicalArmorI = intent.getFloatExtra("physicalArmorI", 0);
        physicalPenI = intent.getFloatExtra("physicalPenI", 0);
        critChanceI = intent.getFloatExtra("critChanceI", 0);
        critDamageI = intent.getFloatExtra("critDamageI", 0);
        attackSpeedI = intent.getFloatExtra("attackSpeedI", 0);
        maxHPI = intent.getFloatExtra("maxHPI", 0);
        maxManaI = intent.getFloatExtra("maxManaI", 0);
        lifestealI = intent.getFloatExtra("lifestealI", 0);
        hpRegenI = intent.getFloatExtra("hpRegenI", 0);
        manaRegenI = intent.getFloatExtra("manaRegenI", 0);
        cooldownReductionI = intent.getFloatExtra("cooldownReductionI", 0);

        //Title bar
        heroNameString = intent.getStringExtra("HeroName");
        TextView heroName = (TextView)findViewById(R.id.heroName);
        heroName.setText("Detailed Stats for " + heroNameString);

        //Display stats at top
        TextView energyDamageTV = (TextView) findViewById(R.id.energyDamage);
        TextView energyArmorTV = (TextView) findViewById(R.id.energyArmor);
        TextView energyPenTV = (TextView) findViewById(R.id.energyPen);
        TextView physicalDamageTV = (TextView) findViewById(R.id.physicalDamage);
        TextView physicalArmorTV = (TextView) findViewById(R.id.physicalArmor);
        TextView physicalPenTV = (TextView) findViewById(R.id.physicalPen);
        TextView critChanceTV = (TextView) findViewById(R.id.critChance);
        TextView critDamageTV = (TextView) findViewById(R.id.critDamage);
        TextView attackSpeedTV = (TextView) findViewById(R.id.attackSpeed);
        TextView maxHPTV = (TextView) findViewById(R.id.maxHP);
        TextView maxManaTV = (TextView) findViewById(R.id.maxMana);
        TextView lifestealTV = (TextView) findViewById(R.id.lifesteal);
        TextView hpRegenTV = (TextView) findViewById(R.id.hpRegen);
        TextView manaRegenTV = (TextView) findViewById(R.id.manaRegen);
        TextView cooldownReductionTV = (TextView) findViewById(R.id.cooldownReduction);

        energyDamageTV.setText(String.format("%d", Math.round(energyDamageI)));
        energyArmorTV.setText(String.format("%d", Math.round(energyArmorI)));
        energyPenTV.setText(String.format("%d", Math.round(energyPenI)));
        physicalDamageTV.setText(String.format("%d", Math.round(physicalDamageI)));
        physicalArmorTV.setText(String.format("%d", Math.round(physicalArmorI)));
        physicalPenTV.setText(String.format("%d", Math.round(physicalPenI)));
        critChanceTV.setText(String.format("%d", Math.round(critChanceI)));
        critDamageTV.setText(String.format("%d", Math.round(critDamageI)));
        attackSpeedTV.setText(String.format("%d", Math.round(attackSpeedI)));
        maxHPTV.setText(String.format("%d", Math.round(maxHPI)));
        maxManaTV.setText(String.format("%d", Math.round(maxManaI)));
        lifestealTV.setText(String.format("%d", Math.round(lifestealI)));
        hpRegenTV.setText(String.format("%d", Math.round(hpRegenI)));
        manaRegenTV.setText(String.format("%d", Math.round(manaRegenI)));
        cooldownReductionTV.setText(String.format("%d", Math.round(cooldownReductionI)));

        //Max HP and Mana
        TextView maxHP = (TextView)findViewById(R.id.maxHPValue);
        TextView maxMana = (TextView)findViewById(R.id.maxManaValue);
        maxHP.setText(String.format("%d", Math.round(maxHPI + baseHP)));
        maxMana.setText(String.format("%d", Math.round(maxManaI + baseMana)));

        //DPS Calculations
        TextView dpsValue = (TextView)findViewById(R.id.dps);
        TextView baseAuto = (TextView)findViewById(R.id.baseAuto);
        TextView critAuto = (TextView)findViewById(R.id.critAuto);
        TextView attackPS = (TextView)findViewById(R.id.attackPS);

        //Auto attack damage
        attackDamage = baseEDMG + basePDMG + energyDamageI + physicalDamageI;
        baseAuto.setText(String.format("%d", Math.round(attackDamage)));

        //Crit auto damage
        critMulti = critMulti + critDamageI;
        critDamage = attackDamage * (critMulti/100);
        critAuto.setText(String.format("%d", Math.round(critDamage)));

        //Attacks per second
        attackSpeed = 1/(100/(baseASR+attackSpeedI)*baseAT);
        double roundedAS = (double) Math.round(attackSpeed*100)/100;
        attackPS.setText(String.format("%.2f",roundedAS));

        //DPS
        dps = (float)roundedAS * attackDamage;
        critDPS = (dps * (critChanceI/100) * (critMulti/100)) + (dps * (1-(critChanceI/100)));
        dpsValue.setText(String.format("%.2f", critDPS));
    }
}
