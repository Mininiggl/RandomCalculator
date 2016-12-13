package hs_hannover.de.randomcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends Activity {
    private CountDownTimer countDownTimer;
    private int SummandA;
    private int SummandB;
    private int Summe;
    private boolean RichtigeSumme;
    private int Punkte;
    private int level;
    TextView textSummandA;
    TextView textSummandB;
    TextView textSumme;
    TextView textTimer;
    TextView textPunkte;
    Button btnRichtig;
    Button btnFalsch;
    private final long STARTTIME = 5; //Timer in Sekunden
    private final double INTERVAL = 0.1;  //Interval des Timers in Sekunden


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSummandA = (TextView) findViewById(R.id.textSummandA);
        textSummandB = (TextView) findViewById(R.id.textSummandB);
        textSumme = (TextView) findViewById(R.id.textSumme);
        textTimer = (TextView) findViewById(R.id.textTimer);
        textPunkte = (TextView) findViewById(R.id.textPunkte);
        btnRichtig = (Button) findViewById(R.id.btnRichtig);
        btnFalsch = (Button) findViewById(R.id.btnFalsch);
        Spinner spinner = (Spinner) findViewById(R.id.spinDifficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinDifficulty,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        zufallGenerier();
        ((Spinner)findViewById(R.id.spinDifficulty)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = position;
                Log.d("Level",String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((Button) findViewById(R.id.btnRichtig)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                berechnungPunkte(RichtigeSumme);
            }
        });
        ((Button) findViewById(R.id.btnFalsch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                berechnungPunkte(!RichtigeSumme);
            }
        });
    }

    public void zufallGenerier(){
        zufallZahlen(0);
        countDownTimer = new MyCountDownTimer(STARTTIME*1000, (long)(INTERVAL*1000));
        countDownTimer.start();
    }

    public void berechnungPunkte(boolean richtigeAntwort){
        if(richtigeAntwort){
            Punkte += 10;
            textPunkte.setText(String.valueOf(Punkte));
            countDownTimer.cancel();
            countDownTimer = new MyCountDownTimer(STARTTIME*1000, (long)(INTERVAL*1000));
            countDownTimer.start();
            zufallZahlen(level);
        }else{
            countDownTimer.cancel();
            btnRichtig.setEnabled(false);
            btnFalsch.setEnabled(false);
        }
    }

    public void zufallZahlen(int level){
        Random r = new Random();
        SummandA = r.nextInt((10*(int) Math.pow(10,level)))+1;
        textSummandA.setText(String.valueOf(SummandA));
        SummandB = r.nextInt((10*(int) Math.pow(10,level)))+1;
        textSummandB.setText(String.valueOf(SummandB));
        RichtigeSumme = r.nextBoolean();
        if(RichtigeSumme){
            Summe = SummandA + SummandB;
        }else{
            do{
                Summe = r.nextInt(20*(int)Math.pow(10,level))+1;
                Log.d("DebugMode",String.valueOf(Summe));
            }while (Summe == SummandA+SummandB);
        }

        Log.d("RichtigeSumme", String.valueOf(RichtigeSumme));
        textSumme.setText(String.valueOf(Summe));
    }
    public class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long startTime, long interval){
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            textTimer.setText("Timer zuende");
            btnRichtig.setEnabled(false);
            btnFalsch.setEnabled(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textTimer.setText(String.valueOf(millisUntilFinished/100));
        }
    }

}

