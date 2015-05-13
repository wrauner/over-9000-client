package com.mkoi.over9000.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * @author Wojciech Rauner
 */

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface UserPreferences {

    String token();

    String nick();

}
