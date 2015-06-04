package com.mkoi.over9000.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Interejs do typesafe userpreferences
 * @author Wojciech Rauner
 */

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface UserPreferences {

    String token();

    String nick();

}
