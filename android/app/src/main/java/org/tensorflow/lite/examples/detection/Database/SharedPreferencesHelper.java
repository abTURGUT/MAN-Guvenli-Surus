package org.tensorflow.lite.examples.detection.Database;

import android.content.Context;
import android.content.SharedPreferences;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;

public class SharedPreferencesHelper {

    public Kurumsal getKurumsal(Context context) {
        String id = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_id", "");
        String firstName = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_firstName", "");
        String lastName = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_lastName", "");
        String mail = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_mail", "");
        String password = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_password", "");
        String phoneNumber = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_phoneNumber", "");
        String companyName = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("kurumsal_companyName"," ");
        Boolean rememberMe = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getBoolean("kurumsal_rememberMe", false);

        Kurumsal kurumsal = new Kurumsal(id, firstName, lastName, mail, password, phoneNumber, companyName, rememberMe);

        return kurumsal;
    }

    public Bireysel getBireysel(Context context) {
        String id = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_id", "");
        String firstName = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_firstName", "");
        String lastName = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_lastName", "");
        String mail = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_mail", "");
        String password = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_password", "");
        String phoneNumber = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_phoneNumber", "");
        String rootId = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("bireysel_rootId"," ");
        Boolean rememberMe = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getBoolean("bireysel_rememberMe", false);

        Bireysel bireysel = new Bireysel(id, firstName, lastName, mail, password, phoneNumber, rootId, rememberMe);

        return bireysel;
    }

    public void setKurumsal(Context context, Kurumsal kurumsal) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString( "kurumsal_id", kurumsal.getId());
        edit.putString( "kurumsal_firstName", kurumsal.getFirstName());
        edit.putString( "kurumsal_lastName", kurumsal.getLastName());
        edit.putString( "kurumsal_mail", kurumsal.getMail());
        edit.putString( "kurumsal_password", kurumsal.getPassword());
        edit.putString( "kurumsal_phoneNumber", kurumsal.getPhoneNumber());
        edit.putString( "kurumsal_companyName", kurumsal.getCompanyName());
        edit.putBoolean( "kurumsal_rememberMe", kurumsal.isRememberMe());
        edit.commit();
    }

    public void setBireysel(Context context, Bireysel bireysel) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString( "bireysel_id", bireysel.getId());
        edit.putString( "bireysel_firstName", bireysel.getFirstName());
        edit.putString( "bireysel_lastName", bireysel.getLastName());
        edit.putString( "bireysel_mail", bireysel.getMail());
        edit.putString( "bireysel_password", bireysel.getPassword());
        edit.putString( "bireysel_phoneNumber", bireysel.getPhoneNumber());
        edit.putString( "bireysel_rootId", bireysel.getRootId());
        edit.putBoolean( "bireysel_rememberMe", bireysel.isRememberMe());
        edit.commit();
    }

    public String getLoginType(Context context) {
        String loginType = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString("loginType", "");
        return loginType;
    }

    public void setLoginType(Context context, String loginType) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString( "loginType", loginType);
        edit.commit();
    }

}
