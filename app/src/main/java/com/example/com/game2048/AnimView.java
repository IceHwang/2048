package com.example.com.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;

/**
 * Created by HFH on 2017/9/9.
 */

public class AnimView extends GridLayout {

    private Card cards[][]=new  Card[4][4];

    private int cardwidth;

    private static AnimView animView=null;

    public static AnimView getAnimView() {return animView;}


    public AnimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        animView=this;
        initAnimView();
    }

    public AnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animView=this;
        initAnimView();
    }

    public AnimView(Context context) {
        super(context);
        animView=this;
        initAnimView();
    }


    private void initAnimView()
    {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);


    }


    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh)
    {
        super.onSizeChanged(w,h,oldh,oldh);
        cardwidth=(Math.min(w,h)-10)/4;
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
                c.setNum(0);

                addView(c,width,height);

                cards[x][y]=c;


            }
        }
    }

    public void moveAnim(final int from_x, final int to_x, final int from_y, final int to_y, final int num)
    {
        cards[from_x][from_y].setNum(num);

        TranslateAnimation anim = new TranslateAnimation(0,(to_x-from_x)*cardwidth,0,(to_y-from_y)*cardwidth);
        anim.setDuration(250);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                cards[from_x][from_y].setNum(0);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        anim.setAnimationListener(animationListener);

        cards[from_x][from_y].startAnimation(anim);
    }

}
