package ru.skillbranch.devintensive.extensions

fun String.truncate(value: Int = 16): String {
    return if (this.trimEnd().length <= value) {
        this
    } else {
        val s = this.substring(0, value)
        val s2 = s.trimEnd()
        if (s.length == s2.length) {
            "$s..."
        } else {
            "$s2..."
        }
    }
}

fun String.stripHtml(): String {
    var s = this
    var x = s.indexOf('<')
    var y = s.indexOf('>')
    while (x > -1 && y > -1) {
        s = s.removeRange(x, y + 1)
        x = s.indexOf('<')
        y = s.indexOf('>')
    }

    val pattern = "\\s+".toRegex()
    s = s.replace(pattern, " ")

    return s
}
