package com.mkoi.over9000.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * @author Wojciech Rauner
 */

@SharedPref
public interface UserPreferences {

    String salt();

    String email();

}
