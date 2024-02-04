package com.example.smtpserver.util;

import java.util.Base64;

public class Base64Decoder {
    public static String decode(String text){
        return new String(Base64.getDecoder().decode(text));
    }
}
