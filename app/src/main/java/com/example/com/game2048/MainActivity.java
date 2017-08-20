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

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;


    private static Button Restart,Withdraw;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();
        best_score=preferences.getInt("BestScore",0);

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


        Restart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameView.getGameView().gamestart();
                addScore(0);
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
            editor.clear();
        }
        t_current_score.setText(""+current_score);
        if(add==2048)
        {
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
                            editor.clear();
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
                            Toast.makeText(MainActivity.this,"佩服佩服,解锁成就:无聊到爆炸",Toast.LENGTH_LONG).show();
                            GameView.getGameView().gamestart();
                            addScore(0);
                        }
                    }
            ).show();
        }

    }


    public void gameover()
    {
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
}

