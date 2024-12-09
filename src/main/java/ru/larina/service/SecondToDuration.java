package ru.larina.service;

import java.time.Duration;

public class SecondToDuration {
    public static Duration getDurationfromSeconds(Long nanoSeconds) {
        int seconds = (int) (nanoSeconds / 1000000000);
        int days = seconds / (60 * 60 * 24);
        int daySeconds = seconds - days * 60 * 60 * 24;
        int hours = daySeconds / (60 * 60);
        int hourSeconds = daySeconds - hours * 60 * 60;
        int minutes = hourSeconds / 60;
        int secs = hourSeconds % 60;
        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(secs);
    }
}
