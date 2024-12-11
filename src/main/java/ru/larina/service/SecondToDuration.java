package ru.larina.service;

import java.time.Duration;

public class SecondToDuration {
    public static Duration getDurationfromSeconds(final Long nanoSeconds) {
        final int seconds = (int) (nanoSeconds / 1000000000);
        final int days = seconds / (60 * 60 * 24);
        final int daySeconds = seconds - days * 60 * 60 * 24;
        final int hours = daySeconds / (60 * 60);
        final int hourSeconds = daySeconds - hours * 60 * 60;
        final int minutes = hourSeconds / 60;
        final int secs = hourSeconds % 60;
        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(secs);
    }
}
