package com.example.mymusicdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.mymusicdemo.databinding.ActivityMainBinding;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int count =0;
    String title="";

    MediaPlayer music=null;
    ArrayList<Integer> songlistArray;
    MyHandler handler = new MyHandler();


    String currentlenth ="--:--";
//    int[] songlist = {R.raw.tera_fitoor,R.raw.merijaan,R.raw.rataanranta};
    int n=0;
    songprogressBar  seekbarProg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        songlistArray = new ArrayList<>();
        songlistArray.add(R.raw.merijaan);
        songlistArray.add(R.raw.rataanranta);
        songlistArray.add(R.raw.tera_fitoor);


       music = MediaPlayer.create(MainActivity.this,songlistArray.get(n));

       binding.seekBar.setEnabled(false);






        // Play pause Action
        binding.imagePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0) {
                    play();
                }else{
                    pause();
                }


            }
        });
        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//
                binding.imagePlayBtn.setImageResource(R.drawable.playbtn);
                count=0;

            }
        });

        //next song action
        binding.imageNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                if (n < songlistArray.size()&& n>-1){

                    music.stop();
                    music.reset();
                    music.release();
                    music = null;

                    music = MediaPlayer.create(MainActivity.this,songlistArray.get(n));

                    setLengthOfSong();


                    play();

                }
                else{
                    Toast.makeText(MainActivity.this, "song full", Toast.LENGTH_SHORT).show();



                }




            }
        });

        // previous btn code
            binding.imagePreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    n--;
                    if (n == -1) {
                        n=0;
                    }
                    if (n < songlistArray.size()&& n> -1){

                        music.stop();
                        music.reset();
                        music.release();
                        music = null;

                        music = MediaPlayer.create(MainActivity.this,songlistArray.get(n));
                        setLengthOfSong();
                        play();

                    }
                    else{
                        Toast.makeText(MainActivity.this, "0th song", Toast.LENGTH_SHORT).show();



                    }

                }
            });





    }
    public void  play(){


            binding.imagePlayBtn.setImageResource(R.drawable.pausebtn);
            count=1;
//                    music.seekTo(0);
            music.start();


//                    binding.seekBar.setProgress(music.getCurrentPosition());
            int currentpos = binding.seekBar.getProgress();
            binding.seekBar.setMax(music.getDuration()/1000);

//            seekbarProg = new songprogressBar();;
//            seekbarProg.start();


//        setcurrentlengthThread setCurren = new setcurrentlengthThread();
//        setCurren.start();








        }



    public void pause(){
        binding.imagePlayBtn.setImageResource(R.drawable.playbtn);
        count=0;
        music.pause();

    }

    public void setLengthOfSong(){
        // code for convert milisecond lenth of song in minutes:second forment (00:00)
        String lenthminutes =null;
        int lenth = music.getDuration();
        int minutes = lenth/60000;
        lenthminutes = ""+minutes;
        lenth = lenth%60000;
        int seconds = lenth/1000;

        binding.songLength.setText(""+lenthminutes+":"+seconds);
    }

    class  MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
             Bundle bndl = msg.getData();
             String length = bndl.getString("currentLength");
             binding.textView2.setText(length);
        }
    }



    class songprogressBar extends Thread{
        @Override
        public void run() {
            super.run();
            while (music.isPlaying()){
                int currentprog = music.getCurrentPosition()/1000;

                if(currentprog < binding.seekBar.getMax()){

                    binding.seekBar.setProgress(currentprog+1);

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }

        }
    }

    void setCurrentlenth(){
//        binding.textView2.setText(""+currentlenth);
        while (music.isPlaying()){
            int temp = music.getCurrentPosition();
//                Log.e("time : ","time :"+temp);
            if(temp<music.getDuration()){

                int m= temp/60000;
                temp%=60000;
                int s = temp/1000;
                currentlenth = ""+m+":"+s;
                Log.e("time",currentlenth);
                Bundle bundle = new Bundle();
                bundle.putString("CurrentLenght",currentlenth);
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);



            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
    class setcurrentlengthThread extends  Thread{
        @Override
        public void run() {
            super.run();
            setCurrentlenth();
        }
    }
}