package com.example.paualtet.squared;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameConfig extends AppCompatActivity {

    private static final int ACTION_PLAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

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
    }

    //update method for the size seekbar
    private void updateSizeBar(int progress) {
        TextView t = (TextView) findViewById(R.id.sizeTxt);
        t.setText(getString(R.string.size) + (progress + 3));
    }

    //start play
    protected void startPlay() {

        Intent i = new Intent(this, GameField.class);

        SeekBar s = (SeekBar) findViewById(R.id.sizeSeekBar);
        i.putExtra("size", s.getProgress());

        startActivity(i);
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ACTION_PLAY:

            }
        }
    }*/
}

