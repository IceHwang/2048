package com.example.com.game2048;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HHB on 2017/8/19.
 */

public class GameView extends GridLayout {

    private final int m_left = 0, m_right = 1, m_up = 2, m_down = 3;

    public Card[][] cards = new Card[4][4];

    private Card[][] pre_cards = new Card[4][4];

    private Card[][] tmp_cards = new Card[4][4];


    private int pre_score;

    private List<Point> emptyPoints = new ArrayList<Point>();

    private static GameView gameView = null;

    public static GameView getGameView() {
        return gameView;
    }

    public int cardwidth;

    public void withdraw() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                cards[x][y].setNum(pre_cards[x][y].getNum());
                Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                animator.setTarget(cards[x][y]);
                animator.start();
            }
        }

        MainActivity.getMainActivity().addScore(pre_score - MainActivity.getMainActivity().getCurrent_score());
    }


    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gameView = this;
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameView = this;
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        gameView = this;
        initGameView();
    }


    private void initGameView() {
        setColumnCount(4);
        //setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {

            private double down_x, down_y, delta_x, delta_y;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down_x = event.getX();
                        down_y = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        delta_x = down_x - event.getX();
                        delta_y = down_y - event.getY();

                        if (Math.abs(delta_x) > Math.abs(delta_y))//横向滑动
                        {
                            if (delta_x > 5) {
                                move(m_left);
                            } else if (delta_x < -5) {
                                move(m_right);
                            }
                        } else {
                            if (delta_y > 5) {
                                move(m_up);
                            } else if (delta_y < -5) {
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldh, oldh);
        cardwidth = (Math.min(w, h) - 10) / 4;
        addCards(cardwidth, cardwidth);
        gamestart();

    }

    private void addCards(int width, int height) {
        Card c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, width, height);

                cards[x][y] = c;

                c = new Card(getContext());
                c.setNum(0);
                pre_cards[x][y] = c;

                c = new Card(getContext());
                c.setNum(0);
                tmp_cards[x][y] = c;
            }
        }
    }

    private void move(int direc) {

        int tmp_score = MainActivity.getMainActivity().getCurrent_score();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                tmp_cards[x][y].setNum(cards[x][y].getNum());
            }
        }


        boolean flag = false;

        switch (direc) {
            case m_left:
                flag = MoveLeft();
                break;
            case m_right:
                flag = MoveRight();
                break;
            case m_up:
                flag = MoveUp();
                break;
            case m_down:
                flag = MoveDown();
                break;
        }
        if (flag == true)//移动成功
        {
            MainActivity.getMainActivity().withdraw_flag = true;
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    pre_cards[x][y].setNum(tmp_cards[x][y].getNum());
                }
            }
            pre_score = tmp_score;


            if (addRandomNum() == false)//添加卡片并验证游戏是否结束,,,,,,逻辑错误,无法验证
            {
                checkgameover();
            }


        } else {
            //移动失败,没有可以移动的卡片
        }


    }

    private void checkgameover()//在卡片填满后检验还能否消去
    {
        for (int y = 0; y < 4; y++) {
            for (int x = 1; x < 4; x++) {
                if (cards[x - 1][y].getNum() == cards[x][y].getNum()) {
                    return;//可消去
                }
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                if (cards[x][y - 1].getNum() == cards[x][y].getNum()) {
                    return;//可消去
                }
            }
        }

        //不可消去
        MainActivity.getMainActivity().gameover();
    }


    public void gamestart() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cards[x][y].setNum(0);
            }
        }
        MainActivity.current_score = 0;
        addRandomNum();
        addRandomNum();

    }


    private boolean addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cards[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }


        Point point = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));

        cards[point.x][point.y].setNum(Math.random() > 0.1 ? 2 : 4);

        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.add_card);
        animator.setTarget(cards[point.x][point.y]);

        animator.start();

        if (emptyPoints.size() == 0) {

            return false;
        }

        return true;

    }






    private boolean MoveLeft()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {
            for(int x=1;x<4;x++)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int xx=x-1;xx>=-1;xx--)
                {
                    if(xx==-1)//移动到边际
                    {
                        xx++;

                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[xx][y].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[xx][y].equals(cards[x][y]))//移动到发生消除
                    {
                        MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        xx++;

                        if(xx==x)
                            break;


                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean MoveRight()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {
            for(int x=2;x>=0;x--)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int xx=x+1;xx<=4;xx++)
                {
                    if(xx==4)//移动到边际
                    {
                        xx--;

                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[xx][y].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[xx][y].equals(cards[x][y]))//移动到发生消除
                    {
                        MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        xx--;

                        if(xx==x)
                            break;


                        AnimView.getAnimView().moveAnim(x,xx,y,y,cards[x][y].getNum());
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[xx][y]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean MoveUp()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {
            for(int y=1;y<4;y++)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int yy=y-1;yy>=-1;yy--)
                {
                    if(yy==-1)//移动到边际
                    {
                        yy++;

                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[x][yy].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[x][yy].equals(cards[x][y]))//移动到发生消除
                    {
                        MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        yy++;

                        if(yy==y)
                            break;


                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean MoveDown()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {
            for(int y=2;y>=0;y--)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int yy=y+1;yy<=4;yy++)
                {
                    if(yy==4)//移动到边际
                    {
                        yy--;

                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[x][yy].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[x][yy].equals(cards[x][y]))//移动到发生消除
                    {
                        MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        yy--;

                        if(yy==y)
                            break;


                        AnimView.getAnimView().moveAnim(x,x,y,yy,cards[x][y].getNum());
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        animator.setTarget(cards[x][yy]);
                        animator.setStartDelay(250);
                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }



}
