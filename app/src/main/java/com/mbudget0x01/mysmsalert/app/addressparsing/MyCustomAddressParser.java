package com.mbudget0x01.mysmsalert.app.addressparsing;
import android.content.Context;
import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;

import java.io.EOFException;

public class MyCustomAddressParser implements IAdressParser {

    public MyCustomAddressParser(Context pContext){
        myContext = pContext;
    }

    Context myContext;


    //Parses City,Address,Rest to -> Address, City
    //dono anymore why not split but had a reason
    public String tryParseAddress(String pMessage) throws EOFException {
        if (!pMessage.contains(",")){
            throw new EOFException("Could not parse Adress");
        }
        String sCity,sAddress,sUserCity;
        sUserCity = SettingsHandler.getUserCity(myContext);
        sCity = "Stupid Compiler";
        int index = 0;
        if (!(sUserCity.isEmpty() || sUserCity.trim().isEmpty())){
            if (pMessage.toLowerCase().contains(sUserCity.toLowerCase())){
                sCity = sUserCity;
                index= sCity.length()+1;}
            }else{

                int cityStart = 0;
                index = 0;

                while ((pMessage.charAt(index)!= ',' && pMessage.charAt(index)!= ' ')|| index <= 5){
                    index ++;
                }
                sCity = pMessage.substring(cityStart, index);
                index ++;

            }
        int firstAdrrIndex = index;
        while (pMessage.charAt(index)!= ','){
            index ++;
        }
        index ++;

        sAddress = pMessage.substring(firstAdrrIndex, index);
        sAddress = sAddress.trim();
        return sAddress + " " + sCity;
        }
    }
