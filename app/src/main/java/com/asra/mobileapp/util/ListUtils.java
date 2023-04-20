package com.asra.mobileapp.util;

import java.util.List;

public class ListUtils {

    public static boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(List list){
        return list != null && list.size() > 0;
    }

    public static int getListSize(List list){
        return list == null ? 0 :  list.size() ;
    }


}
