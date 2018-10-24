package irwan.lampungresto.Kelas;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Glory on 19/09/2018.
 */

public class UserPreference {
    private String KEY_BAGIAN = "bagian";
    private SharedPreferences preferences;

    public UserPreference(Context context){
        String PREFS_NAME = "UserPref";
        preferences = context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
    }

    public void setBagian(String bagian){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_BAGIAN,bagian);
        editor.apply();
    }

    public String getBagian(){
        return preferences.getString(KEY_BAGIAN,null);
    }
}
