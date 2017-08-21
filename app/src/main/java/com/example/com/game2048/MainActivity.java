package com.example.com.game2048;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int[] achieveflag;

    private int regret,clickRestart,achievegameover;


    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;


    private static Button Restart,Withdraw,Achievement;

    private static TextView t_current_score;
    private static TextView t_best_score;



    public static int current_score=0;

    public static int best_score=0;


    public boolean withdraw_flag=false;

    private static MainActivity mainActivity = null;

    public MainActivity()
    {
        mainActivity = this;
    }

    public static MainActivity getMainActivity()
    {
        return mainActivity;
    }

    public int getCurrent_score()
    {
        return current_score;
    }

    private String boolToString(int flag)
    {
        if(flag==1)
            return "完成";
        else
            return "未完成";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();
        best_score=preferences.getInt("BestScore",0);
        achieveflag=new int[8];

        achieveflag[0]=preferences.getInt("achievement0",0);
        achieveflag[1]=preferences.getInt("achievement1",0);
        achieveflag[2]=preferences.getInt("achievement2",0);
        achieveflag[3]=preferences.getInt("achievement3",0);
        achieveflag[4]=preferences.getInt("achievement4",0);
        achieveflag[5]=preferences.getInt("achievement5",0);
        achieveflag[6]=preferences.getInt("achievement6",0);
        achieveflag[7]=preferences.getInt("achievement7",0);

        regret=preferences.getInt("regret",0);
        clickRestart=preferences.getInt("clickrestart",0);
        achievegameover=preferences.getInt("achievegameover",0);

        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        t_current_score=(TextView)findViewById(R.id.current_score);
        t_best_score=(TextView)findViewById(R.id.best_score);
        t_best_score.setText(""+best_score);
        Restart=(Button)findViewById(R.id.restart);
        Withdraw=(Button)findViewById(R.id.withdraw);
        Achievement=(Button)findViewById(R.id.achievement);




        Achievement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("成就");
                dialog.setMessage(""
                        +"有点无聊:最高分达到2500\n"+"                              ................"+boolToString(achieveflag[0])+"\n"
                        +"是很无聊:最高分达到3500\n"+"                              ................"+boolToString(achieveflag[1])+"\n"
                        +"丢人先锋:后悔50次\n"+"                              ................"+boolToString(achieveflag[2])+"\n"
                        +"控分大佬:不到150分结束游戏\n"+"                              ................"+boolToString(achieveflag[3])+"\n"
                        +"别点,谢谢:重开200次\n"+"                              ................"+boolToString(achieveflag[4])+"\n"
                        +"注意节制:游戏结束50次\n"+"                              ................"+boolToString(achieveflag[5])+"\n"
                        +"您是大佬:集成一个2048\n"+"                              ................"+boolToString(achieveflag[6])+"\n"
                        +"强迫症患者:完成以上所有成就\n"+"                              ................"+boolToString(achieveflag[7])+"\n"



                );
                dialog.setCancelable(false);
                dialog.setNegativeButton("朕知道了",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface,int which)
                            {

                            }
                        }
                );
                dialog.setPositiveButton("什么狗屎",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface,int which)
                            {

                            }
                        }
                ).show();
            }
        });

        Restart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameView.getGameView().gamestart();
                addScore(0);
                clickRestart++;
                editor.putInt("clickrestart",clickRestart);
                editor.apply();
                achieveCheck4();
            }
        });


        Withdraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this,"你好弱哦 (´∇｀)",Toast.LENGTH_LONG).show();
                if(withdraw_flag==true)
                {
                    withdraw_flag=false;
                    regret++;
                    editor.putInt("regret",regret);
                    editor.apply();
                    achieveCheck2();
                    GameView.getGameView().withdraw();
                }
            }
        });
    }

    public void addScore(int add)
    {

        current_score+=add;
        if (current_score>best_score)
        {
            best_score=current_score;
            t_best_score.setText(""+best_score);
            editor.putInt("BestScore",current_score);
            editor.apply();
        }
        t_current_score.setText(""+current_score);
        achieveCheck0();

        if(add==2048)
        {
            achieveCheck6();
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("无敌无敌");
            dialog.setMessage("你开挂了吧,对吧,对吧?你就承认了吧");
            dialog.setCancelable(false);
            dialog.setPositiveButton("我凭本事的开的挂,怎么",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int which)
                        {
                            best_score=0;
                            t_best_score.setText(""+best_score);
                            editor.putInt("BestScore",current_score);
                            editor.apply();
                            Toast.makeText(MainActivity.this,"成绩清零,以示惩罚ヽ( ￣д￣;)ノ",Toast.LENGTH_LONG).show();
                            GameView.getGameView().gamestart();
                            addScore(0);
                        }
                    }
            );
            dialog.setNegativeButton("没有,你不要乱说",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int which)
                        {
                            Toast.makeText(MainActivity.this,"佩服佩服",Toast.LENGTH_LONG).show();
                            GameView.getGameView().gamestart();
                            addScore(0);
                        }
                    }
            ).show();
        }

    }


    public void gameover()
    {
        achievegameover++;
        achieveCheck5();
        achieveCheck3();
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("");
        dialog.setMessage("游戏结束");
        dialog.setCancelable(false);
        dialog.setPositiveButton("那又怎样",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int which)
                    {

                    }
                }
        );
        dialog.setNegativeButton("认输认输",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int which)
                    {
                        GameView.getGameView().gamestart();
                        addScore(0);
                    }
                }
        ).show();


    }

    public void achieveCheck0()
    {
        if(achieveflag[0]==0)
        {
            if(best_score>2500)
            {
                achieveflag[0]=1;
                editor.putInt("achievement0",1);
                editor.apply();

                Toast.makeText(MainActivity.this,"完成成就:有点无聊",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck1();
        achieveCheck7();
    }


    public void achieveCheck1()
    {
        if(achieveflag[1]==0)
        {
            if(best_score>3500)
            {
                achieveflag[1]=1;
                editor.putInt("achievement1",1);
                editor.apply();

                Toast.makeText(MainActivity.this,"完成成就:是很无聊",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck7();
    }
    public void achieveCheck2()
    {
        if(achieveflag[2]==0)
        {
            if(regret>=30)
            {
                achieveflag[2]=1;
                editor.putInt("achievement2",1);
                editor.apply();

                Toast.makeText(MainActivity.this,"完成成就:丢人先锋",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck7();
    }

    public void achieveCheck3()
    {
        if(achieveflag[3]==0)
        {
            if(current_score<150)
            {
                achieveflag[3]=1;
                editor.putInt("achievement3",1);
                editor.apply();

                Toast.makeText(MainActivity.this,"完成成就:控分大佬",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void achieveCheck4()
    {
        if(achieveflag[4]==0)
        {
            if(clickRestart>=200)
            {
                achieveflag[4]=1;
                editor.putInt("achievement4",1);
                editor.apply();

                Toast.makeText(MainActivity.this,"完成成就:别点,谢谢",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck7();
    }
    public void achieveCheck5()
    {
        if(achieveflag[5]==0)
        {
            if(achievegameover>=50)
            {
                achieveflag[5]=1;
                editor.putInt("achievement5",1);
                editor.apply();


                Toast.makeText(MainActivity.this,"完成成就:注意节制",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck7();
    }
    public void achieveCheck6()
    {
        if(achieveflag[6]==0)
        {
            if(true)
            {
                achieveflag[6]=1;
                editor.putInt("achievement6",1);
                editor.apply();


                Toast.makeText(MainActivity.this,"完成成就:您是大佬",Toast.LENGTH_LONG).show();
            }
        }
        achieveCheck7();
    }
    public void achieveCheck7()
    {
        boolean flag=true;
        if(achieveflag[7]==0)
        {
            for(int x=0;x<7;x++)
            {
                if(achieveflag[x]==0)
                {
                    flag=false;
                    break;
                }
            }
            if(flag)
            {
                achieveflag[7]=1;
                editor.putInt("achievement7",1);
                editor.apply();


                Toast.makeText(MainActivity.this,"完成成就:强迫症患者",Toast.LENGTH_LONG).show();
            }
        }
    }


}

