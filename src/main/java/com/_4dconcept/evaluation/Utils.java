package com._4dconcept.evaluation;

import java.util.List;

public class Utils {

   public static boolean isStringNullOrEmptyOrBlank(String string){
        if(string == null)
            return true;

        if(string.isEmpty())
            return true;

       String stringTrim = string.trim();
       if(stringTrim.isEmpty())
           return true;

       return false;
    }

    public static boolean isListNullOrEmpty(List list){
        if(list == null)
            return true;

        if(list.isEmpty())
            return true;

        return false;
    }

}
