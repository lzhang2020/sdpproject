package edu.gatech.seclass.sdpscramble.utils;


import java.util.ArrayList;

public class HelperUtils {

    public static boolean isInList(String user, ArrayList<String> userList){
        boolean inList = false;
        for (String s : userList){
            if (user.equals(s)){
                inList = true;
                break;
            }
        }
        return inList;
    }
}
