package com.vansuita.bitfit.extension

import java.text.DateFormat
import java.util.Date

fun Date.asDate(): String = DateFormat.getDateInstance().format(this)

fun Long.asDate(): String = Date(this).asDate()