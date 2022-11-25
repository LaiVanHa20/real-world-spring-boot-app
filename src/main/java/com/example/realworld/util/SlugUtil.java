package com.example.realworld.util;

import javax.xml.crypto.Data;
import java.util.Date;

public class SlugUtil {
    public static String getSlug(String title){
        return title.toLowerCase().replaceAll("\\s+", "-") +"-"+ new Date().getTime();
    }
}
