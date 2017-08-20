package com.example.com.game2048;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by HHB on 2017/8/19.
 */

public class GameView extends GridLayout {

    private final int m_left=0,m_right=1,m_up=2,m_down=3;

    private Card [][] cards = new Card[4][4];

    private List<Point> emptyPoints = new ArrayList<Point>();

    private static GameView gameView=null;

    public static GameView getGameView()
    {
        return gameView;
    }




    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gameView=this;
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameView=this;
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        gameView=this;
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
        gamestart();

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

    private void move(int direc)
    {
        boolean flag =false;

        switch (direc)
        {
            case m_left:
                flag=MoveLeft();
                break;
            case m_right:
                flag=MoveRight();
                break;
            case m_up:
                flag=MoveUp();
                break;
            case m_down:
                flag=MoveDown();
                break;
        }
        if(flag==true)
        {
            if(addRandomNum()==false)//添加卡片并验证游戏是否结束,,,,,,逻辑错误,无法验证
            {
                checkgameover();
            }

        }
        else
        {
            //移动失败,没有可以移动的卡片
        }


    }

    private void checkgameover()//在卡片填满后检验还能否消去
    {
        for(int y=0;y<4;y++)
        {
            for(int x=1;x<4;x++)
            {
                if(cards[x-1][y].getNum()==cards[x][y].getNum())
                {
                    return;//可消去
                }
            }
        }

        for(int x=0;x<4;x++)
        {
            for(int y=1;y<4;y++)
            {
                if(cards[x][y-1].getNum()==cards[x][y].getNum())
                {
                    return;//可消去
                }
            }
        }

        //不可消去
        MainActivity.getMainActivity().gameover();
    }


    public void gamestart()
    {
        for (int y=0;y<4;y++)
        {
            for (int x=0;x<4;x++)
            {
                cards[x][y].setNum(0);
            }
        }
        MainActivity.current_score=0;
        addRandomNum();
        addRandomNum();

    }



    private boolean addRandomNum()
    {
        emptyPoints.clear();
        for (int y=0;y<4;y++)
        {
            for (int x=0;x<4;x++)
            {
                if(cards[x][y].getNum()==0)
                {
                    emptyPoints.add(new Point(x,y));
                }
            }
        }



        Point point=emptyPoints.remove((int)(Math.random()*emptyPoints.size()));

        cards[point.x][point.y].setNum(Math.random()>0.1?2:4);

        if(emptyPoints.size()==0)
        {

            return false;
        }

        return true;

    }


    private boolean MoveLeft()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {


            for(int x=1;x<4;x++)//尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int xx;
                    for(xx=x-1;xx>=0;xx--)
                    {
                        if (cards[xx][y].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    xx++;
                    if(xx==x)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

            for(int x=1;x<4;x++)//尝试消除
            {
                if(cards[x-1][y].getNum()==cards[x][y].getNum()&&cards[x][y].getNum()!=0)//消除成功
                {
                    MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);
                    cards[x-1][y].setNum(cards[x][y].getNum()*2);
                    cards[x][y].setNum(0);
                    flag=true;
                }
            }

            for(int x=1;x<4;x++)//可能发生了消除,再次尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int xx;
                    for(xx=x-1;xx>=0;xx--)
                    {
                        if (cards[xx][y].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    xx++;
                    if(xx==x)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

        }


        if(flag==false)
            return false;
        else
            return true;
    }

    private boolean MoveRight()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {


            for(int x=2;x>=0;x--)//尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int xx;
                    for(xx=x+1;xx<=3;xx++)
                    {
                        if (cards[xx][y].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    xx--;
                    if(xx==x)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

            for(int x=2;x>=0;x--)//尝试消除
            {
                if(cards[x+1][y].getNum()==cards[x][y].getNum()&&cards[x][y].getNum()!=0)//消除成功
                {
                    MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);
                    cards[x+1][y].setNum(cards[x][y].getNum()*2);
                    cards[x][y].setNum(0);
                    flag=true;
                }
            }

            for(int x=2;x>=0;x--)//可能发生了消除,再次尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int xx;
                    for(xx=x+1;xx<=3;xx++)
                    {
                        if (cards[xx][y].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    xx--;
                    if(xx==x)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

        }


        if(flag==false)
            return false;
        else
            return true;
    }

    private boolean MoveUp()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {


            for(int y=1;y<4;y++)//尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int yy;
                    for(yy=y-1;yy>=0;yy--)
                    {
                        if (cards[x][yy].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    yy++;
                    if(yy==y)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

            for(int y=1;y<4;y++)//尝试消除
            {
                if(cards[x][y-1].getNum()==cards[x][y].getNum()&&cards[x][y].getNum()!=0)//消除成功
                {
                    MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);
                    cards[x][y-1].setNum(cards[x][y].getNum()*2);
                    cards[x][y].setNum(0);
                    flag=true;
                }
            }

            for(int y=1;y<4;y++)//可能发生了消除,再次尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int yy;
                    for(yy=y-1;yy>=0;yy--)
                    {
                        if (cards[x][yy].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    yy++;
                    if(yy==y)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

        }


        if(flag==false)
            return false;
        else
            return true;
    }

    private boolean MoveDown()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {


            for(int y=2;y>=0;y--)//尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int yy;
                    for(yy=y+1;yy<=3;yy++)
                    {
                        if (cards[x][yy].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    yy--;
                    if(yy==y)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

            for(int y=2;y>=0;y--)//尝试消除
            {
                if(cards[x][y+1].getNum()==cards[x][y].getNum()&&cards[x][y].getNum()!=0)//消除成功
                {
                    MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);
                    cards[x][y+1].setNum(cards[x][y].getNum()*2);
                    cards[x][y].setNum(0);
                    flag=true;
                }
            }

            for(int y=2;y>=0;y--)//可能发生了消除,再次尝试移动
            {
                if(cards[x][y].getNum()!=0)//有一个卡片尝试移动
                {
                    int yy;
                    for(yy=y+1;yy<=3;yy++)
                    {
                        if (cards[x][yy].getNum()!=0)//遇到阻碍,移动结束
                        {
                            break;
                        }
                    }

                    yy--;
                    if(yy==y)//移动失败
                    {

                    }
                    else//移动成功
                    {
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);
                        flag=true;
                    }


                }
            }

        }


        if(flag==false)
            return false;
        else
            return true;
    }
}
