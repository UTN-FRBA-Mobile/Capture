package com.example.dadmapp.utils

import java.text.Normalizer

fun normalizeText(text: String?): String {
    var normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
    normalizedText = normalizedText.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    return normalizedText
}