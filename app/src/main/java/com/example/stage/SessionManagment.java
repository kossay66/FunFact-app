package com.example.stage;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SherdPrefName = "session";
    String Session_Key = "Session_User";

    public SessionManagment(Context context) {
        sharedPreferences = context.getSharedPreferences(SherdPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void saveSession(String userID) {
        editor.putString(Session_Key, userID).commit();


    }

    public String getSession() {
        return sharedPreferences.getString(Session_Key, "noID");
    }

    public void removeSession() {
        editor.putString(Session_Key, "noID").commit();
    }


}
