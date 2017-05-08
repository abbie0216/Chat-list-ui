package abbie.chatlistui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Abbie on 2017/5/8.
 */

public class DBHelper extends SQLiteOpenHelper {

    private String TABLE_NAME = "Friends";
    private String _ID = "_id";
    private String NAME = "name";
    private String LIST_ORDER = "list_order";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY NOT NULL," +
                NAME + " TEXT," +
                LIST_ORDER + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void addFriend(String name, int order) {
        Log.d("Abbie", "addFriend " + name + "(order=" + order + ")");
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(LIST_ORDER, order);
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void deleteFriend(int id) {
        Log.d("Abbie", "deleteFriend id=" + id);
        getWritableDatabase().delete(TABLE_NAME, _ID + " = " + id, null);
    }

    public void swapOrder(FriendItem itemA, FriendItem itemB) {
        int idA = itemA.getId(), idB = itemB.getId();
        int orderA = itemA.getOrder(), orderB = itemB.getOrder();
        Log.d("Abbie", "switchOrder id[" + idA + "]'s order[" + orderA + "=>" + orderB + "]");
        Log.d("Abbie", "switchOrder id[" + idB + "]'s order[" + orderB + "=>" + orderA + "]");
        getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + LIST_ORDER + "=" + orderB + " WHERE " + _ID + "=" + idA);
        getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + LIST_ORDER + "=" + orderA + " WHERE " + _ID + "=" + idB);
    }

    public ArrayList<FriendItem> getAllFriends() {
        ArrayList<FriendItem> friends = new ArrayList<>();
        String[] columns = {_ID, NAME, LIST_ORDER};
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, columns, null, null, null, null, LIST_ORDER + " ASC");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int order = cursor.getInt(2);
            friends.add(new FriendItem(id, name, order));
        }
        return friends;
    }
}
