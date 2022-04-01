package com.kiettc.nomtudien;


import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.Editable;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class sql extends SQLiteOpenHelper {
    private Context mycontext;
    private ArrayList<HanNom> hanNoms;
    private static String DB_NAME = "db.db";//the extension may be .sqlite or .db
    private String DB_PATH ;
    public SQLiteDatabase myDataBase;
    public sql(Context context, int i) throws IOException {
        super(context,DB_NAME,null,1);
        this.mycontext=context;
        boolean dbexist = checkdatabase();
        if (dbexist) {
            //System.out.println("Database exists");
            opendatabase();
        } else {
            System.out.println("Database doesn't exist");
            createdatabase();
        }
    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(dbexist) {
            //System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {

        InputStream myinput = mycontext.getAssets().open(DB_NAME);


        String outfilename = DB_PATH + DB_NAME;


        OutputStream myoutput = new FileOutputStream(outfilename);

        byte[] buffer = new byte[2048];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        System.out.println("coppying suceesful");

        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    public ArrayList<HanNom> searchInNom(String string){
        hanNoms = new ArrayList<>();
        opendatabase();
        Cursor cursor = myDataBase.rawQuery("select * from tbl_dict_nom where char like \"%"+string+"%\" or vietnamese like \""+string+"\" order by vietnamese ASC",null);
        cursor.moveToFirst();
        try{
            do{
                System.out.println("hhhh: "+cursor.getString(1));
                String chars=cursor.getString(1);
                String amV=cursor.getString(2);
                String unicode = cursor.getString(0);
                HanNom hn = new HanNom(chars,amV,"---",unicode,"Không");
                hanNoms.add(hn);
                cursor.moveToNext();
            }while (!cursor.isNull(0));
            cursor.close();

            if (cursor.isClosed()){
                System.out.println("is closed");
            }
        }catch (CursorIndexOutOfBoundsException exception){

        }
        return hanNoms;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<HanNom> searchInHan(String string) {
        hanNoms = new ArrayList<>();
        opendatabase();
        Cursor cursor = myDataBase.rawQuery("select * from tbl_dict_han where char like \"%" + string + "%\" or vietnamese like \"%" + string + "%\" order by vietnamese ASC", null);
        cursor.moveToFirst();
        try {
            do {
                System.out.println("hhhh: " + cursor.getString(1));
                String chars = cursor.getString(1);
                String cachVietK;
                if (chars.length()>1){
                    cachVietK=chars.substring(1,chars.length());
                }else cachVietK = "";
                chars=String.valueOf(chars.charAt(0));
                String amK = cursor.getString(2);
                String unicode = cursor.getString(0);
                String meanings = cursor.getString(3);
                //Xu ly am doc
                String[] a;
                if (amK.indexOf(",")>-1){
                    a=amK.split(",");
                }else a= amK.split(" ");
                hanNoms.add(new HanNom(chars,a[0], meanings, unicode, amK,cachVietK));
                cursor.moveToNext();
            } while (!cursor.isNull(0));
            cursor.close();

            if (cursor.isClosed()) {
                System.out.println("is closed");
            }
        } catch (CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
        return hanNoms;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        DB_PATH ="/data/data/" + mycontext.getPackageName() + "/databases/";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}