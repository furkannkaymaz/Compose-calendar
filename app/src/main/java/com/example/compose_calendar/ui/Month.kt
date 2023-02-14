package com.example.compose_calendar.ui

enum class Month(val displayName: String, val days: (Int) -> Int) {
    JANUARY("Ocak", { year: Int -> 31 }),
    FEBRUARY("Şubat", { year: Int -> if (year % 4 == 0) 29 else 28 }),
    MARCH("Mart", { year: Int -> 31 }),
    APRIL("Nisan", { year: Int -> 30 }),
    MAY("Mayıs", { year: Int -> 31 }),
    JUNE("Haziran", { year: Int -> 30 }),
    JULY("Temmuz", { year: Int -> 31 }),
    AUGUST("Ağustos", { year: Int -> 31 }),
    SEPTEMBER("Eylül", { year: Int -> 30 }),
    OCTOBER("Ekim", { year: Int -> 31 }),
    NOVEMBER("Kasım", { year: Int -> 30 }),
    DECEMBER("Aralık", { year: Int -> 31 });
}