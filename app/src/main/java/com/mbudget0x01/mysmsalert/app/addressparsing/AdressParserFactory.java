package com.mbudget0x01.mysmsalert.app.addressparsing;
import android.content.Context;

import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;

public class AdressParserFactory {

    public static IAdressParser getAddressParser(Context context){
        switch (SettingsHandler.getSelectedAdressParser(context)){
            case "MyCustom":
                return new MyCustomAddressParser(context);
            default:
                return new MyCustomAddressParser(context);
        }
    }
}
