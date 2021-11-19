package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.net.Uri;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final static String SHAPEFILE = "game.txt";

    private TextView helpBox;

    int currentColor;
    int guessColor;
    int scoreYes = 0;
    int scoreNo = 0;
    String name, email;
    int rating;

    public void displayResult(String result){
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public void guessYes(View view){
        if (guessColor == currentColor){
            scoreYes++;
        }
    }

    public void guessNo(View view){
        if (guessColor != currentColor){
            scoreNo++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        helpBox =  (TextView) findViewById(R.id.gameHelp);

        TextView textHeader = findViewById(R.id.header);
        textHeader.setText("Чи співпадає назва кольору зліва з кольором техта зправа?");
        Bundle arguments = getIntent().getExtras();
        if (arguments!=null){
            name = arguments.get("name").toString();
            email = arguments.get("email").toString();
        }
        run();

    }

    public void run(){
        TextView textViewRight = findViewById(R.id.textViewRight);
        TextView textViewLeft = findViewById(R.id.textViewLeft);
        //        TextView textViewScore = findViewById(R.id.textViewScore);
        Random rand = new Random();
        currentColor = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        textViewLeft.setBackgroundColor(currentColor);

        new CountDownTimer(60000,2000){
            public void onTick(long millisUntilFinished){
//                textViewRight.setText("seconds: " + millisUntilFinished / 2000);
                guessColor = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                textViewRight.setBackgroundColor(guessColor);
            }
            public void onFinish(){
//                textViewScore.setText("score yes: " + scoreYes + " score no: " + scoreNo);
                displayResult("All done: ");
                rating = scoreYes+scoreNo;
//                Button startBtn = (Button) findViewById(R.id.sendEmail);
//                startBtn.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View view) {
//                        sendEmail();
//                    }
//                });


            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_open_settings:
                openFile(SHAPEFILE);
                return true;
            case R.id.action_save_settings:
                saveFile(SHAPEFILE);
                return true;
            default:
                return true;
        }
    }

    private void openFile(String fileName){
        try {
            InputStream inputStream = openFileInput(fileName);
            if (inputStream != null){
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuffer builder = new StringBuffer();
                while ((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }
                inputStream.close();
                //
                helpBox.setText(builder.toString());
            }

        } catch(Throwable t) {
            try {
                OutputStream outputStream = openFileOutput(fileName, 0);
                OutputStreamWriter osw = new OutputStreamWriter((outputStream));
                osw.write(helpBox.getText().toString());
                osw.close();
            }catch (Throwable th){
                Toast.makeText(getApplicationContext(), "Exception: " + th.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveFile(String fileName){
        try {
            OutputStream outputStream = openFileOutput(fileName, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(helpBox.getText().toString());
            osw.close();
        }catch (Throwable t){
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();

        }
    }

    protected void sendEmail() {
        Log.i("Send email", "Email message sended successfully");

        String[] TO = {"couchjanus@gmail.com"};
        String[] CC = {"janusnic@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "Finished sending email");
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(GameActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }


    public void toResult(View v){

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("name", name.toString());
        intent.putExtra("email", email.toString());
        intent.putExtra("rating", rating);
        startActivity(intent);
    }


}