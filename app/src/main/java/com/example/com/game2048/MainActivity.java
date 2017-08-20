package com.example.com.game2048;

import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static Button Restart,Withdraw;

    private static TextView t_score;

    public static int num_score=0;

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
        setContentView(R.layout.activity_main);
        t_score=(TextView)findViewById(R.id.score);
        Restart=(Button)findViewById(R.id.restart);
        Withdraw=(Button)findViewById(R.id.withdraw);
        Restart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameView.getGameView().gamestart();
            }
        });
    }

    public void addScore(int add)
    {
        num_score+=add;
        t_score.setText(""+num_score);

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

