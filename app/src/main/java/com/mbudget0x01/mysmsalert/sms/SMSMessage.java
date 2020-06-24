package com.mbudget0x01.mysmsalert.sms;
public class SMSMessage {

    public String sentFrom, message;
    public long timeStamp;

    public SMSMessage(String pSentFrom, String  pMessage, long pTimeStamp){
        sentFrom = pSentFrom;
        message = pMessage;
        timeStamp = pTimeStamp;
    }
}
