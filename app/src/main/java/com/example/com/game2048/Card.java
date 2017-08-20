package com.example.com.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by HHB on 2017/8/19.
 */

public class Card extends FrameLayout {

    private TextView textView;

    private int num;


    public Card(Context context)
    {
        super(context);
        textView=new TextView(getContext());
        textView.setTextSize(32);
        textView.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(-1,-1);//-1指填充满
        lp.setMargins(10,10,0,0);
        addView(textView,lp);
        setNum(0);

    }



    public void setNum(int num)
    {
        this.num=num;
        if(num>0)
        {
            textView.setText(""+num);

        }
        else
        {
            textView.setText("");
        }
        switch (num) {
            case 0:
                textView.setBackgroundColor(0x33ffffff);
                break;
            case 2:
                textView.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                textView.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                textView.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                textView.setBackgroundColor(0xfff59563);
                break;
            case 32:
                textView.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                textView.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                textView.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                textView.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                textView.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                textView.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                textView.setBackgroundColor(0xffedc22e);
                break;
            default:
                textView.setBackgroundColor(0xff3c3a32);
                break;
        }
    }

    public int getNum()
    {
        return num;
    }


    public  boolean equals(Card o)
    {
        return num==o.num;
    }



}
