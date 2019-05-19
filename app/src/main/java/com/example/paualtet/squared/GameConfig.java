package com.example.paualtet.squared;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameConfig extends AppCompatActivity {

    private static final int ACTION_PLAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //start game when button pressed
        Button btn = (Button) findViewById(R.id.startBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               startPlay();
            }
        });


        //inizialization of the size string
        TextView t = (TextView) findViewById(R.id.sizeTxt);
        t.setText(getString(R.string.size) + 3);

        //inizialization of the size seekbar
        SeekBar size = (SeekBar) findViewById(R.id.sizeSeekBar);
        size.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        updateSizeBar(seekBar.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );


        updateBestScore(((SeekBar) findViewById(R.id.sizeSeekBar)).getProgress() + 3);

        Button link = (Button) findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/paltet/Squared"));
                startActivity(intent);
            }
        });

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("How To Play");
        builder1.setMessage("Slide the cells to rearrange them. You will win once they are in ascending order.");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        findViewById(R.id.howto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = builder1.create();
                alert.show();
            }
        });

    }

    //update method for the size seekbar
    private void updateSizeBar(int progress) {
        TextView t = (TextView) findViewById(R.id.sizeTxt);
        t.setText(getString(R.string.size) + (progress + 3));

        updateBestScore(progress + 3);
    }

    //start play
    protected void startPlay() {

        Intent i = new Intent(this, GameField.class);

        SeekBar s = (SeekBar) findViewById(R.id.sizeSeekBar);
        i.putExtra("size", s.getProgress());

        startActivity(i);
    }

    private void updateBestScore(int size){
        TextView tv = (TextView) findViewById(R.id.textMaxScore);
        String key;
        switch(size){
            case 3:
                key = "topScore3";
                break;
            case 4:
                key = "topScore4";
                break;
            case 5:
                key = "topScore5";
                break;
            case 6:
                key = "topScore6";
                break;
            default:
                key = "topScore0";
                break;
        };

        SharedPreferences myScore = getSharedPreferences("TopScore", Context.MODE_PRIVATE);
        int bestScore = myScore.getInt(key, 0);
        if (bestScore != 0) tv.setText("Best Score: " + bestScore);
        else tv.setText("");
    }
}

