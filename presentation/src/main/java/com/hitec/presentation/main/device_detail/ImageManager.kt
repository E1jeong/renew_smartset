package com.hitec.presentation.main.device_detail

object ImageManager {
    private const val METER_OLD = "meter old"
    private const val METER_NEW = "meter new"
    private const val TERMINAL_OLD = "terminal old"
    private const val TERMINAL_NEW = "terminal new"
    private const val BACKGROUND = "background"
    private const val SURROUNDINGS = "surroundings"
    private const val BEFORE_AS = "before as"
    private const val AFTER_AS = "after as"

    fun parsePhotoTypeCd(photoTypeCd: Int): String {
        return when (photoTypeCd) {
            1 -> METER_OLD
            2 -> METER_NEW
            3 -> TERMINAL_OLD
            4 -> TERMINAL_NEW
            5 -> BACKGROUND
            6 -> SURROUNDINGS
            7 -> BEFORE_AS
            8 -> AFTER_AS
            else -> ""
        }
    }
}