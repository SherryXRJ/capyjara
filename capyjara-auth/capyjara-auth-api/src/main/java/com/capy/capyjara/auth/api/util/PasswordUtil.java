package com.capy.capyjara.auth.api.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PasswordUtil {

    private final static char[][] KEYBOARD = {
            {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'},
            {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'},
            {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\'},
            {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|'},
            {'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\''},
            {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '"'},
            {'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/'},
            {'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?'},
            {'1','q','a','z'},
            {'!','Q','A','Z'},
            {'2','w','s','x'},
            {'@','W','S','X'},
            {'3','e','d','c'},
            {'#','E','D','C'},
            {'4','r','f','v'},
            {'$','R','F','V'},
            {'5','t','g','b'},
            {'%','T','G','B'},
            {'6','y','h','n'},
            {'^','Y','H','N'},
            {'7','u','j','m'},
            {'&','U','J','M'},
            {'8','i','k',','},
            {'*','I','K','<'},
            {'9','o','l','.'},
            {'(','O','L','>'},
            {'(','O','L','>'},
            {'0','p',';','/'},
            {')','P',':','?'},
            {'-','[','\''},
            {'_','{','"'},

    };

    public static boolean isConsecutive(String password, int length) {
        if (!StringUtils.hasText(password)){
            return false;
        }
        String afterDistinct = password.chars()
                .mapToObj(i -> String.valueOf((char) i))
                .distinct().collect(Collectors.joining());

        int passwordLength = afterDistinct.length();

        for (int i = 0; i <= passwordLength - length; i++) {
            char firstChar = afterDistinct.charAt(i);
            String toCheckStr = afterDistinct.substring(i, i + length);

            for (char[] chars : KEYBOARD) {
                for (int k = 0; k < chars.length; k++) {
                    if (chars[k] == firstChar) {
                        String table = new String(Arrays.copyOfRange(chars, k, k + length));
                        if (table.equals(toCheckStr)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
