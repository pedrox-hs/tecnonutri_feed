package com.pedrenrique.tecnonutri.data.net.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal class GsonUTCDateAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val dateFormatStr = "yyyy-MM-dd" // "yyyy-MM-dd'T'HH:mm:ss.SSS"
    private val dateFormat: DateFormat = SimpleDateFormat(dateFormatStr, Locale.US)
        .apply { timeZone = TimeZone.getTimeZone("UTC") }

    @Synchronized
    override fun serialize(
        date: Date,
        type: Type,
        context: JsonSerializationContext
    ): JsonElement = JsonPrimitive(dateFormat.format(date))

    @Synchronized
    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): Date? =
        try {
            dateFormat.parse(json.asString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
}