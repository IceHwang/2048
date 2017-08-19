package com.example.com.game2048;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.content.Context;



/**
 * Created by HHB on 2017/8/19.
 */

public class GameView extends GridLayout {

    private final int m_left=0,m_right=1,m_up=2,m_down=3;

    private Card [][] cards = new Card[4][4];

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
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {

            private double down_x,down_y,delta_x,delta_y;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        down_x=event.getX();
                        down_y=event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        delta_x=down_x-event.getX();
                        delta_y=down_y-event.getY();

                        if( Math.abs(delta_x)>Math.abs(delta_y) )//横向滑动
                        {
                            if (delta_x>5)
                            {
                                move(m_left);
                            }
                            else if(delta_x<-5)
                            {
                                move(m_right);
                            }
                        }
                        else
                        {
                            if (delta_y>5)
                            {
                                move(m_up);
                            }
                            else if(delta_y<-5)
                            {
                                move(m_down);
                            }

                        }


                        break;

                }



                return true;
            }
        });

    }


    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh)
    {
        super.onSizeChanged(w,h,oldh,oldh);
        int cardwidth=(Math.min(w,h)-10)/4;
        addCards(cardwidth,cardwidth);

    }

    private void addCards(int width,int height)
    {
        Card c;

        for (int y=0;y<4;y++)
        {
            for(int x=0;x<4;x++)
            {
                c=new Card(getContext());
                c.setNum(2);
                addView(c,width,height);

                cards[x][y]=c;
            }
        }
    }

    private void move(int direc)
    {

    }

}
