package com.example.com.game2048;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;


    private static Button Restart,Withdraw;

    private static TextView t_current_score;
    private static TextView t_best_score;


    public static int current_score=0;

    public static int best_score=0;


    private static MainActivity mainActivity = null;

    public MainActivity()
    {
        mainActivity = this;
    }

    public static MainActivity getMainActivity()
    {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();
        best_score=preferences.getInt("BestScore",0);

        setContentView(R.layout.activity_main);
        t_current_score=(TextView)findViewById(R.id.current_score);
        t_best_score=(TextView)findViewById(R.id.best_score) ;
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
    }

    public void addScore(int add)
    {
        current_score+=add;
        if (current_score>best_score)
        {
            best_score=current_score;
            t_best_score.setText(""+best_score);
            editor.putInt("BestScore",current_score);
        }
        t_current_score.setText(""+current_score);


    }


    public void gameover()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("");
        dialog.setMessage("Game Over");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int which)
                    {

                    }
                }
        ).show();
    }
}

