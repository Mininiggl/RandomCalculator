package hs_hannover.de.randomcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {
    private int SummandA;
    private int SummandB;
    private int Summe;
    TextView textSummandA;
    TextView textSummandB;
    TextView textSumme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSummandA = (TextView) findViewById(R.id.textSummandA);
        textSummandB = (TextView) findViewById(R.id.textSummandB);
        textSumme = (TextView) findViewById(R.id.textSumme);
        zufallGenerier();
    }

    public void zufallGenerier(){
        zufallZahlen(0);
    }

    public void zufallZahlen(int level){
        Random r = new Random();
        SummandA = r.nextInt((10*(int) Math.pow(10,level)))+1;
        textSummandA.setText(String.valueOf(SummandA));
        SummandB = r.nextInt((10*(int) Math.pow(10,level)))+1;
        textSummandB.setText(String.valueOf(SummandB));
        Summe = SummandA + SummandB;
        textSumme.setText(String.valueOf(Summe));
    }


}
