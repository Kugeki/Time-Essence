package com.example.timeessence;

public class NumeralsEndingConverter {
    public static String convertMinutes(long minutes) {
        String s;
        long digit = minutes % 10;
        long decade = (minutes / 10) % 10;

        if (decade == 1 || digit == 0) {
            return minutes + " минут";
        }
        if (digit == 1) {
            return minutes + " минута";
        }
        if (digit == 2 || digit == 3 || digit == 4) {
            return minutes + " минуты";
        }

        return minutes + " минут"; // для 5, 6, 7, 8, 9
    }
}
