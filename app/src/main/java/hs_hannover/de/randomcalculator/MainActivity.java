package hs_hannover.de.randomcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class MainActivity extends Activity {
    private CountDownTimer countDownTimer;
    private long TimerValue;
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
    Button btnStart;
    Spinner spinner;
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
        btnStart = (Button) findViewById(R.id.btnStart);
        spinner = (Spinner) findViewById(R.id.spinDifficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinDifficulty,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        btnRichtig.setEnabled(false);
        btnFalsch.setEnabled(false);
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
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zufallGenerier();
                btnStart.setEnabled(false);
                btnFalsch.setEnabled(true);
                btnRichtig.setEnabled(true);
                spinner.setEnabled(false);
            }
        });
    }

    public void zufallGenerier(){
        Punkte = 0;
        textPunkte.setText(String.valueOf(Punkte));
        zufallZahlen(level);
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
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(getApplicationContext(), "Falsche Antwort - GAME OVER", Toast.LENGTH_SHORT).show();
            btnRichtig.setEnabled(false);
            btnFalsch.setEnabled(false);
            btnStart.setEnabled(true);
            spinner.setEnabled(true);
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
            //long [] pattern = {250, 250, 250, 250};
            textTimer.setText("Timer abgelaufen");
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            Toast.makeText(getApplicationContext(), "Zeit abgelaufen - GAME OVER", Toast.LENGTH_SHORT).show();
            TimerValue = 0;
            btnRichtig.setEnabled(false);
            btnFalsch.setEnabled(false);
            btnStart.setEnabled(true);
            spinner.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            TimerValue = millisUntilFinished;
            Log.d("timerstate", String.valueOf(TimerValue));
            textTimer.setText(String.valueOf(millisUntilFinished/100));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Punkte = savedInstanceState.getInt("punktzahl");
        textPunkte.setText(String.valueOf(Punkte));
        SummandA = savedInstanceState.getInt("SummandA");
        textSummandA.setText(String.valueOf(SummandA));
        SummandB = savedInstanceState.getInt("SummandB");
        textSummandB.setText(String.valueOf(SummandB));
        Summe = savedInstanceState.getInt("Summe");
        textSumme.setText(String.valueOf(Summe));
        RichtigeSumme= savedInstanceState.getBoolean("richtigeSumme");
        btnRichtig.setEnabled(savedInstanceState.getBoolean("btnRichtig"));
        btnFalsch.setEnabled(savedInstanceState.getBoolean("btnFalsch"));
        btnStart.setEnabled(savedInstanceState.getBoolean("btnStart"));
        level = savedInstanceState.getInt("Level");
        long timerstate = savedInstanceState.getLong("timerstate");
        if(timerstate != 0) {
            countDownTimer = new MyCountDownTimer(timerstate, (long) (INTERVAL * 1000));
            countDownTimer.start();
        }
        spinner.setSelection(level);
        spinner.setEnabled(savedInstanceState.getBoolean("spinnerEnabled"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("punktzahl", Punkte);
        outState.putInt("SummandA",SummandA);
        outState.putInt("SummandB",SummandB);
        outState.putInt("Summe",Summe);
        outState.putBoolean("richtigeSumme",RichtigeSumme);
        outState.putBoolean("btnRichtig", btnRichtig.isEnabled());
        outState.putBoolean("btnFalsch", btnFalsch.isEnabled());
        outState.putBoolean("btnStart", btnStart.isEnabled());
        outState.putInt("Level", level);
        if(TimerValue != 0){
            countDownTimer.cancel();
        }

        outState.putLong("timerstate", TimerValue);
        Log.d("timerstate", String.valueOf(TimerValue));
        outState.putBoolean("spinnerEnabled", spinner.isEnabled());
    }
}

