package ru.netology.nmedia.utils


fun Int.formatCount(): String {
    return when {
        this >= 1_000_000 -> {
            val million = this / 100_000
            (million / 10.0).toString() + "M"
        }

        this >= 10_000 -> (this / 1000).toString() + "K"
        this >= 1_000 -> {
            val thousand = this / 100
            (thousand / 10.0).toString() + "K"
        }

        else -> this.toString()
    }
}


