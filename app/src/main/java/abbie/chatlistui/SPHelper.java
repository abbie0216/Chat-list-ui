package abbie.chatlistui;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Abbie on 2017/5/8.
 */

public class SPHelper {

    public static SPHelper instance;
    private Context ctx;
    private SharedPreferences sp;

    public SPHelper(Context ctx){
        sp = ctx.getSharedPreferences("DATA", 0);
    }

    public static void init(Context ctx){
        instance = new SPHelper(ctx);
    }

    public static SPHelper getInstance(){
        return instance;
    }

    public void putUserName(String name){
        putData("USER_NAME", name);
    }

    public String getUserName(){
        return getData("USER_NAME");
    }


    private void putData(String key, String value){
        sp.edit().putString(key, value).commit();
    }

    private String getData(String key){
        return sp.getString(key,"");
    }
}
