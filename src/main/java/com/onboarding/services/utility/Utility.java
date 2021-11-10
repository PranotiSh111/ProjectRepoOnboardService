package com.onboarding.services.utility;

import java.util.List;

public class Utility {

    public static Boolean isNullOrEmpty(String message){
        return (null != message && !message.isEmpty());
    }

    public static Boolean isNullOrEmptyList(List list){
        return (null != list && !list.isEmpty());
    }

}
