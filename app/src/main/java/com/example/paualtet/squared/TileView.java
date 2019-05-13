package com.example.paualtet.squared;

import android.content.Context;
import android.widget.Button;

class TileView extends android.support.v7.widget.AppCompatButton{

    public int x, y, value, background;

    public TileView(Context context, int x, int y, int value, int background){
        super(context);
        this.x = x;
        this.y = y;
        this.value = value;
        setBackgroundResource(background);
        this.background = background;
    }
}
