package ru.mvi.core.utils

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri

fun TextView.setHtml(html: String, handleClick: (Uri) -> Unit) {
    val charSequence = Html.fromHtml(html)
    val strBuilder = SpannableStringBuilder(charSequence)
    val urls = strBuilder.getSpans(0, charSequence.length, URLSpan::class.java)
    urls.forEach {
        makeLinkClickable(strBuilder, it, handleClick)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}


private fun makeLinkClickable(
    strBuilder: SpannableStringBuilder,
    span: URLSpan,
    handleClick: (Uri) -> Unit
) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)

    val clickable = object : ClickableSpan() {
        override fun onClick(widget: View) {
            handleClick(span.url.toUri())
        }
    }

    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}