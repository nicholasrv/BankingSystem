package com.example.BankingSystem.utility;

public class ConvertToLong {
//    public static void main(String args[]){
//        System.out.println(convertToLong(10));
//    }

    public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }

}

