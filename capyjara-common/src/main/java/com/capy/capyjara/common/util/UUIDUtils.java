package com.capy.capyjara.common.util;

import java.util.Random;
import java.util.UUID;

public class UUIDUtils {

    private static Random rdm = new Random();


    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }



}
