package com.example.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    String userName, userEmail;
    int userRating = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle arguments = getIntent().getExtras();
        if (arguments!=null){
            userName = arguments.get("name").toString();
            userEmail = arguments.get("email").toString();
            userRating = Integer.parseInt(arguments.get("rating").toString());
        }
    }

    public void saveToDb(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS players (name TEXT, email TEXT, rate INTEGER, UNIQUE(name))");
//        String sql = "INSERT OR IGNORE INTO players VALUES("+userName+","+userEmail+","+3+");";
//        db.execSQL(sql);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", userName);
        contentValues.put("email", userEmail);
        contentValues.put("rate", userRating);
        db.insert("players", null, contentValues);
//        db.execSQL("INSERT OR IGNORE INTO players VALUES('Tom Cat', 'tom@my.com', 15);");
        db.close();
    }

    public void showResult(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM players WHERE name='"+ userName + "'", null);

        TextView showName = findViewById(R.id.showName);
        TextView showEmail = findViewById(R.id.showEmail);
        TextView showRating = findViewById(R.id.showRating);
        showName.setText("");
        showEmail.setText("");
        showRating.setText("");

        while(query.moveToNext()){
            String name = query.getString(0);
            String email = query.getString(1);
            int rating = query.getInt(2);
            showName.append(name);
            showEmail.append(email);
            showRating.append(""+rating);
        }
        query.close();
        db.close();
    }
}