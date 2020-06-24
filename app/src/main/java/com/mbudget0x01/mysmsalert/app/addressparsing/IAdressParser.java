package com.mbudget0x01.mysmsalert.app.addressparsing;

import java.io.EOFException;

public interface IAdressParser {
    String tryParseAddress(String pMessage) throws EOFException;
}

