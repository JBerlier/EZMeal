package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * TO DO : Intent to etape recette si cancel timer
 *         Intent to etape recette a la fin timer
 *         Sound + vibrate when timer done (maybe)
 *         When run in background, show layout recette + change button to come back to timer
 *         onActivityResult, get time asked
 *
 * Created by Laure on 04/05/2017.
 */

public class TimerActivity extends Activity {
    private static final String TAG="TimerActivity";
    private static final int REQUEST_CODE=1;

    private int time_tospend=10, percent_progress=0;
    private String time_progress="";
    private Handler handler=new Handler();
    private boolean flagCancel=false, pauseState=false;

    private ProgressBar progressBar;
    private TextView progressText;
    private Button cancel_button,pause_button,run_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.timer);
        this.progressBar=(ProgressBar)this.findViewById(R.id.progressBar);
        this.progressText=(TextView)this.findViewById(R.id.progressText);
        this.cancel_button=(Button)this.findViewById(R.id.cancel_button);
        this.pause_button=(Button)this.findViewById(R.id.pause_button);
        this.run_background=(Button)this.findViewById(R.id.run_background_button);

        new Thread(new Runnable() {
            @Override
            public void run() {
                long initial_time=System.currentTimeMillis();
                long current_time=initial_time;
                long old_time=current_time;
                while(percent_progress<=100 && !flagCancel) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent_progress);
                            progressText.setText(time_progress);
                        }
                    });
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    current_time=System.currentTimeMillis();
                    if(current_time-old_time>=1000 && !pauseState) {
                        old_time=System.currentTimeMillis();
                        long delta=(old_time-initial_time)/1000;
                        percent_progress=(int)(100*delta/time_tospend);
                        time_progress=getTimeString(time_tospend-delta);
                    }
                    if(pauseState) {
                        long start_pause=System.currentTimeMillis();
                        while(pauseState) {
                            try{
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        long end_pause=System.currentTimeMillis();
                        initial_time=initial_time-start_pause+end_pause;
                    }
                }
                alertMessage();
                // Intent to etape recette
            }
        }).start();

        this.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        this.pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_continue();
            }
        });
        this.run_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_background();
            }
        });
    }

    public String getTimeString(long time) {
        long minutes=time/60;
        long secondes=time%60;
        return Long.toString(minutes)+":"+Long.toString(secondes);
    }
    public void cancel(){
        this.flagCancel=true;
        // Intent etape recette
    }
    public void run_background() {
        //this.setContentView();
    }
    public void pause_continue(){
        if(pauseState) {
            this.pauseState=false;
            this.pause_button.setText(R.string.pause_label);
        } else {
            this.pauseState=true;
            this.pause_button.setText(R.string.continue_label);
        }
    }
    public void alertMessage() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TimerActivity.this);
                alertDialogBuilder.setTitle(R.string.timer_ended_title_label);
                alertDialogBuilder.setMessage(R.string.timer_ended_label);
                alertDialogBuilder.setPositiveButton(R.string.timer_ended_stop_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}