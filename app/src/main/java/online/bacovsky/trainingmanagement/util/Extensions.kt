package online.bacovsky.trainingmanagement.util

import android.content.res.Resources
import java.text.NumberFormat
import java.util.Locale

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val currencySymbol: String?
    get() = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency?.symbol

fun Long.formattedNumber(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getNumberInstance(locale)
    return numberFormat.format(this)
}

