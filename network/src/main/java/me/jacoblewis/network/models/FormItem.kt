package me.jacoblewis.network.models

import androidx.annotation.Keep

@Keep
sealed class FormItem {
    @Keep
    class Text(val text: String) : FormItem()

    @Keep
    class File(val file: java.io.File, val type: String) : FormItem()
}