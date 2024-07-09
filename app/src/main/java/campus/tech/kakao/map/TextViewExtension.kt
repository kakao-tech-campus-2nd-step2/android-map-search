package campus.tech.kakao.map

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

public inline fun TextView.addTextChangedListener(
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: Editable?) -> Unit = {}
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            text: CharSequence?, start: Int, count: Int, after: Int
        ) = beforeTextChanged(text, start, count, after)

        override fun onTextChanged(
            text: CharSequence?, start: Int, before: Int, count: Int
        ) = onTextChanged(text, start, before, count)

        override fun afterTextChanged(text: Editable?) = afterTextChanged(text)
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}