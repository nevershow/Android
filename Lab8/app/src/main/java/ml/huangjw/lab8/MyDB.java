package ml.huangjw.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Huangjw on 2016/11/19.
 */
public class MyDB extends SQLiteOpenHelper {
  private static final String TABLENAME = "lab8";

  private SQLiteDatabase db;

  public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
    db = getWritableDatabase();
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = "create table if not exists " + TABLENAME +
              " (name TEXT PRIMARY KEY, birthMon INTEGER, birthDay INTEGER, gift TEXT)";
    db.execSQL(sql);
  }

  public long insert2DB(String name, int month, int day, String gift) {
    ContentValues cv = new ContentValues();
    cv.put("name", name);
    cv.put("birthMon", month);
    cv.put("birthDay", day);
    cv.put("gift", gift);
    return db.insert(TABLENAME, null, cv);
  }

  public void delete(String name) {
    db.delete(TABLENAME, "name = ?", new String[]{name});
  }

  public void update(String name, int month, int day, String gift) {
    ContentValues cv = new ContentValues();
    cv.put("name", name);
    cv.put("birthMon", month);
    cv.put("birthDay", day);
    cv.put("gift", gift);
    db.update(TABLENAME, cv, "name = ?", new String[]{name});
  }

  public Cursor getAll() {
    return db.rawQuery("select * from lab8", null);
  }
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
