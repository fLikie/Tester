package com.example.tester;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

public class Database {
    public int[] OpenDatabase(Context context, int[] stats) {
        SQLiteDatabase db = context.openOrCreateDatabase("parameters.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM stats;", null);
        if(query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                int parameter = query.getInt(1);
                stats[id] = parameter;
            }
            while (query.moveToNext());
        }
        query.close();
        return stats;
    }
    public boolean CheckDatabase(Context context) {
        File dbFile = context.getDatabasePath("parameters.db");
        return dbFile.exists();
    }

    public void SaveDatabase(Context context, int[] stats) {
        try {
            SQLiteDatabase db = context.openOrCreateDatabase("parameters.db", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS stats (number INTEGER, stat INTEGER)");
            for (int i = 0; i < stats.length; i++) {
                db.execSQL("INSERT INTO stats VALUES (" + i + ", " + stats[i] + ");");
                Log.d("Database", "id " + i + " stat " + stats[i]);
            }
        } catch (SQLException e) {
            Log.d("Database", e.getMessage());
        }
        Log.d("Database", "DB saved");
    }
}
