package com.biedin.yourmoney;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppContext extends Application {

    private static String databaseFolder;
    private static String databasePath;
    private static final String databaseName = "money.db";
    private static final String TAG = "DB";


    public static void checkDatabaseExist(Context context) {
        databaseFolder = context.getApplicationInfo().dataDir + "/" + "databases/";
        databasePath = databaseFolder + databaseName;
        if (!checkDataBaseExists()) {
            copyDataBase(context);
        }
    }

    private static void copyDataBase(Context context) {
        try (InputStream sourceFile = context.getAssets().open(databaseName);
             OutputStream destinationFolder = new FileOutputStream(databasePath)) {
            File dbFolder = new File(databaseFolder);
            dbFolder.mkdir();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceFile.read(buffer)) > 0) {
                destinationFolder.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private static boolean checkDataBaseExists() {
        File dbFile = new File(databasePath);
        return dbFile.exists();
    }


}
