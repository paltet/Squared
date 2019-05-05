package com.example.paualtet.squared;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class GameConfig extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        Button btn = (Button) findViewById(R.id.startBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               startPlay();
            }
        });

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

    private void updateSizeBar(int progress) {

    }

    protected void startPlay() {

    }
}
