package garmter.com.camtalk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTUtils;

public class LectureDB {
    /** Called when the activity is first created. */
    public static final int Maxsub = 20;
    DBHelper dbHelper;
    String[] sname = new String[Maxsub];
    int snameNum = 0;
    public static final String TAG = "ttDbAdapter";

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS subject (_id TEXT PRIMARY KEY, div TEXT, lecture TEXT, timeRoom TEXT, profess TEXT, memo TEXT, extraInfo TEXT);";
    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 7;

    private Context context;

    public LectureDB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void close() {
        dbHelper.close();
    }

    public void DBinsert(List<DKClass> classes) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 0; i < classes.size(); i++) {

            DKClass dk = classes.get(i);

            db.execSQL("INSERT INTO subject VALUES(?,?,?,?,?,?,?);",
                    new Object[] { dk.getCode(), dk.getDiv(), dk.getLecture(), dk.getTimeRoom(), dk.getProfessor(), dk.getMemo(), dk.getExtraInfo() });
        }
        // db.execSQL("INSERT INTO subject VALUES('452750 - 4','컴실','2공522','이상범');");
        // db.execSQL("INSERT INTO subject VALUES('327380 - 2','네트워크','자연517','조경산');");

        if (db != null)
            db.close();
    }

    public void DBclear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from subject ");

        if (db != null)
            db.close();

    }

    public ArrayList<DKClass> DBselect() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<DKClass> classes = new ArrayList<DKClass>();
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT _id, div, lecture, timeRoom, profess, memo, extraInfo FROM subject", null);

            while (cursor.moveToNext()) {
                String code = cursor.getString(0);
                String div = cursor.getString(1);
                String lecture = cursor.getString(2);
                String timeRoom = cursor.getString(3);
                String professor = cursor.getString(4);
                String memo = cursor.getString(5);
                String extraInfo = cursor.getString(6);

                classes.add(new DKClass(code, div, lecture, timeRoom, professor, memo, extraInfo));
            }

        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

        return classes;

    }

    public DKClass getDkClass(String code) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT _id, div, lecture, timeRoom, profess, memo, extraInfo FROM subject where _id like ?", new String[] { code });

            if (cursor.moveToNext()) {
                // String code = cursor.getString(0);
                String div = cursor.getString(1);
                String lecture = cursor.getString(2);
                String timeRoom = cursor.getString(3);
                String professor = cursor.getString(4);
                String memo = cursor.getString(5);
                String extraInfo = cursor.getString(6);

                return new DKClass(code, div, lecture, timeRoom, professor, memo, extraInfo);
            }

        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

        return null;
    }

    public DKClass getCurrentClass(String code) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT _id, div, lecture, timeRoom, profess, memo, extraInfo FROM subject where _id like ?", new String[] { code });

            if (cursor.moveToNext()) {
                // String code = cursor.getString(0);
                String div = cursor.getString(1);
                String lecture = cursor.getString(2);
                String timeRoom = cursor.getString(3);
                String professor = cursor.getString(4);
                String memo = cursor.getString(5);
                String extraInfo = cursor.getString(6);

                return new DKClass(code, div, lecture, timeRoom, professor, memo, extraInfo);
            }

        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

        return null;
    }

    public void updateMemo(String memo, String code) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update subject set memo=? where _id = ?", new String[]{memo, code});
        if (db != null)
            db.close();

    }

    public boolean restoreDB() {

        try {

            String from = CTUtils.getExternalPath() + "/tmp/" + DATABASE_NAME;
            File to = context.getDatabasePath(DATABASE_NAME);

            FileInputStream in = new FileInputStream(from);
            FileOutputStream out = new FileOutputStream(to);

            byte[] buffer = new byte[4096];

            int readSize;
            while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, readSize);
            }

            in.close();
            out.close();

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return false;
    }

    public String backupDB() {

        // /data/data/dk.too.EatOrNot/databases/eatornot.db
        try {
            File inPath = context.getDatabasePath(DATABASE_NAME);
            String outPath = CTUtils.getExternalPath() + "/tmp/" + DATABASE_NAME;
            new File(CTUtils.getExternalPath() + "/tmp/").mkdirs();

            FileInputStream in = new FileInputStream(inPath);
            FileOutputStream out = new FileOutputStream(outPath, false);

            byte[] buffer = new byte[4096];

            int readSize;
            while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, readSize);
            }

            in.close();
            out.close();

            return outPath;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (oldVersion == 6 && newVersion == 7) {

                try {
                    db.beginTransaction();
                    db.execSQL("ALTER TABLE subject ADD COLUMN extraInfo TEXT ");
                    db.setTransactionSuccessful();
                } catch (IllegalStateException e) {
                    Log.d("DB", " migrationInsert6to7 error.", e);
                } finally {
                    db.endTransaction();
                }
                Log.d("DB", " migrationInsert6to7 success.");
            }

        }

    }

}