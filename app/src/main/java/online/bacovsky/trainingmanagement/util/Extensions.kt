package online.bacovsky.trainingmanagement.util

import android.content.res.Resources
import java.text.Normalizer
import java.text.NumberFormat
import java.util.Locale

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val currencySymbol: String?
    get() = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency?.symbol


fun String.removeAccents(): String {
    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}
