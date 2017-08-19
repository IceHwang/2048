package com.example.com.game2048;

import android.util.AttributeSet;
import android.widget.GridLayout;
import android.content.Context;



/**
 * Created by HHB on 2017/8/19.
 */

public class GameView extends GridLayout {

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        initGameView();
    }


    private void initGameView()
    {

    }

}
