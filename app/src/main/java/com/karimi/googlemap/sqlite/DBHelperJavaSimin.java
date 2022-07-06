package com.karimi.googlemap.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelperJavaSimin extends SQLiteOpenHelper {

    private Context mycontext;
    private String DB_PATH;

    private static String DB_NAME = "karimi.db";
    public SQLiteDatabase mDB;


    public DBHelperJavaSimin(Context context){
        super(context,DB_NAME,null,1);
        this.mycontext=context;
        if(android.os.Build.VERSION.SDK_INT >= 4.2){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        boolean dbexist = checkdatabase();
        if (dbexist) {
            opendatabase();
        } else {
            copyDB();
        }

    }

    public void copyDB(){
        boolean dbexist = checkdatabase();
        if(!dbexist) {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = mycontext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DB_NAME;
        mDB = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(mDB != null) {
            mDB.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




//    public String[] getPart(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select part from book " +
//                "group by part order by id",null);
//        String[] parts = new String[cursor.getCount()];
//
//        for (int i = 0; i < parts.length; i++) {
//            cursor.moveToPosition(i);
//            parts[i] = cursor.getString(0);
//        }
//        return parts;
//    }
//    public String[] chapter(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select part from book " +
//                "group by part order by id",null);
//        String[] chapters = new String[cursor.getCount()];
//
//        for (int i = 0; i < chapters.length; i++) {
//            cursor.moveToPosition(i);
//            chapters[i] = cursor.getString(0);
//        }
//        return chapters;
//    }
//
//
//    public String[] getPages(String chapter, String part){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select page from book " +
//                "where chapter='"+chapter+"' and part ='"+part+"'" +
//                "order by id",null);
//        String[] pages = new String[cursor.getCount()];
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            pages[i] = cursor.getString(0);
//        }
//        return pages;
//    }
//    public String getchapterrr(int position){
//        mDB = this.getReadableDatabase();
//        String query = "select part from book group by part order by id";
//        Cursor cursor = mDB.rawQuery(query,null);
//        cursor.moveToPosition(position);
//        return cursor.getString(0);
//    }

//    public int getChapterCount(){
//        mDB = this.getReadableDatabase();
//        String query = "select part from book group by part order by id";
//        Cursor cursor = mDB.rawQuery(query,null);
//        return cursor.getCount();
//    }





//    public String[] getPart(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select part from book",null);
//        String[] parts = new String[cursor.getCount()];
//
//        for (int i = 0; i < parts.length; i++) {
//            cursor.moveToPosition(i);
//            parts[i] = cursor.getString(0);
//        }
//        return parts;
//    }

//
//    public ArrayList<String> getPart(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select terminal_number from user",null);
////        String[] parts = new String[cursor.getCount()];
//        ArrayList<String> bb = new ArrayList<>();
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
////            bb[i] = cursor.getString(0);
//            bb.add(cursor.getString(0));
//
//        }
//        return bb;
//    }


    public ArrayList<String> getTerminal_number(String number){
        mDB = this.getReadableDatabase();
        Cursor cursor = mDB.rawQuery("select * from user where terminal_number = '"+number+"'",null);
        ArrayList<String> bb = new ArrayList<>();

        for (int i = 0; i < cursor.getCount() ; i++) {
            cursor.moveToPosition(i);
            for (int j = 0; j < 11 ; j++) {
            bb.add(cursor.getString(j));
            }
        }
        return bb;
    }



    public int getCount(String number){
        mDB = this.getReadableDatabase();
        Cursor cursor = mDB.rawQuery("select * from user where terminal_number = '"+number+"'",null);
        ArrayList<String> bb = new ArrayList<>();

        String TAG = "qqq-karimi";
//        Log.e(TAG, "getCount: "+cursor.getInt(0) );
        Log.e(TAG, "getCount: "+cursor.getString(11) );


//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            bb.add(cursor.getString(0));
//        }
        return cursor.getCount();
    }








//    public int getCount(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select * from user where phone = 38756834",null);
//        return cursor.getCount();
//    }
//
//    public String getName(){
//        mDB = this.getReadableDatabase();
//        Cursor cursor = mDB.rawQuery("select phone from user where phone = '38756834'",null);
//        return cursor.getString(0);
//    }

}
