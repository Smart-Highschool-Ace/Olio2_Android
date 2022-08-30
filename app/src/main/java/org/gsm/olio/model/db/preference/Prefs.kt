package org.gsm.olio.model.db.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Prefs @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefNm="mPref"
    private val prefs=context.getSharedPreferences(prefNm,MODE_PRIVATE)

    var token:String?
        get() = prefs.getString("token",null)
        set(value){
            prefs.edit().putString("token",value).apply()
        }

    var firstExecution:Boolean
        get() = prefs.getBoolean("isFirst",false)
        set(value){
            prefs.edit().putBoolean("isFirst",value).apply()
        }


}