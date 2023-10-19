package com.hypernym.smartsurveillance.utilities

import android.text.format.DateFormat
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    const val DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun getCurrentDate(): String {
        try {

            val todayDate = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val todayString = formatter.format(todayDate)

           //val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return todayString
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return ""
    }

    /*
    * Returns current date time.
    * */
    fun getCurrentDateTime(dateInUTC: Boolean, time24Hour: Boolean): String {
        lateinit var dateFormat: SimpleDateFormat

        try {
            dateFormat = if (time24Hour)
                SimpleDateFormat("yyyy-MM-dd HH:mm")
            else
                SimpleDateFormat("yyyy-MM-dd hh:mm a")

            if (dateInUTC)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            else
                dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(Date())
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return ""
    }

    /*
    * input: date time in UTC
    * output: returns local date
    * */
    fun getLocalDate(ourDate: String?): String? {
        var ourDate = ourDate
        val inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputDateFormat = "dd MMM, yyyy"

        try {
            val formatter = SimpleDateFormat(inputDateFormat)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)

            val dateFormatter = SimpleDateFormat(outputDateFormat) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
        } catch (e: Exception) {
            e.printStackTrace()
            ourDate = "--"
        }

        return ourDate
    }

    /*
    * input: date time in UTC
    * output: returns local date
    * */
    fun getLocalDate2(ourDate: String?): String? {
        var ourDate = ourDate
        val inputDateFormat = "yyyy-MM-dd'T'HH:mm:SS'Z'"
        val outputDateFormat = "dd MMM, yyyy"

        try {
            val formatter = SimpleDateFormat(inputDateFormat)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)

            val dateFormatter = SimpleDateFormat(outputDateFormat) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
        } catch (e: Exception) {
            e.printStackTrace()
            ourDate = "--"
        }

        return ourDate
    }

    fun getDateTimeFromMillis(time: String): String? {
        val inputFormat = "EEEE, hh:mm aa"

        val longTime = time.toLong()
        val date = Date(longTime)
        val sdf = SimpleDateFormat(inputFormat) // the format of your date
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun getTimeFromMillis(time: String): String? {
        val inputFormat = "hh:mm aa"

        val longTime = time.toLong()
        val date = Date(longTime)
        val sdf = SimpleDateFormat(inputFormat) // the format of your date
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun getDateTimeFromMillis2(time: String): String? {  // Tuesday, 03 April 2021
        val inputFormat = "EEEE, dd MMMM yyyy"

        val longTime = time.toLong()
        val date = Date(longTime)
        val sdf = SimpleDateFormat(inputFormat) // the format of your date
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    /*
    * input: date time in UTC
    * output: returns local date
    * */
    fun getLocalDayMonth(ourDate: String?): String? {
        var ourDate = ourDate
        val inputDateFormat = "yyyy-MM-dd'T'HH:mm:SS'Z'"
        val outputDateFormat = "dd\nMMM"

        try {
            val formatter = SimpleDateFormat(inputDateFormat)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)

            val dateFormatter = SimpleDateFormat(outputDateFormat) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
        } catch (e: Exception) {
            e.printStackTrace()
            ourDate = "--"
        }

        return ourDate
    }

    /*
    * input: date time in UTC
    * output: returns year
    * */
    fun getYear(ourDate: String?): String? {
        var ourDate = ourDate//2020-05-26'T'12:59:03'UTC'
        val inputDateFormat = "yyyy-MM-dd'T'HH:mm:SS'Z'"
        val outputDateFormat = "yyyy"

        try {
            val formatter = SimpleDateFormat(inputDateFormat)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)

            val dateFormatter = SimpleDateFormat(outputDateFormat) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
        } catch (e: Exception) {
            e.printStackTrace()
            ourDate = "--"
        }

        return ourDate
    }

    fun getCurrentTime(dateInUTC: Boolean, time24Hour: Boolean): String {
        lateinit var dateFormat: SimpleDateFormat

        try {
            dateFormat = if (time24Hour)
                SimpleDateFormat("HH:mm")
            else
                SimpleDateFormat("hh:mm a")

            if (dateInUTC)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            else
                dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(Date())
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return ""
    }

    /*
    * Returns first day of current month.
    * */
    fun getFirstDayOfCurrentMonth(): String {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-01")
            return dateFormat.format(Date())
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return ""
    }

    fun getCurrentTimeInMillis(): Long
    {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
    }



    fun getDateUTC(ourDate: String?): String? {
        var ourDate = ourDate
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            formatter.timeZone = TimeZone.getDefault()
            val value = formatter.parse(ourDate)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm") //this format changeable
            dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
            ourDate = dateFormatter.format(value)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ourDate = "00:00 am"
        }
        return ourDate
    }

    fun getElapsedTime(serverTime: String): String {
        val millis = serverTime?.toLong() ?: 0

        val currentParsedDate: Date
        val mInputDateFormat = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())

        try {
            val currentDate =
                DateFormat.format(DATE_INPUT_FORMAT, Calendar.getInstance().time).toString()
            currentParsedDate = mInputDateFormat.parse(currentDate)

            var difference = currentParsedDate.time - millis
            val daysInMilli = 1000 * 60 * 60 * 24
            val elapsedDays = difference / daysInMilli

            // if days have passed
            return if (elapsedDays > 0) {
                getDateTimeFromMillis2(serverTime) ?: "--"
            }
            else { // if the message is of this day.
                getTimeFromMillis(serverTime) ?: "--"
            }
        }
        catch (exception: Exception){
            exception.printStackTrace()
        }

        return "Just Now"
    }

    fun getTimeAgo(datetime: String): String? {
        var datetime = datetime
        val mParsedDate: Date
        val currentParsedDate: Date
        datetime = datetime.replace("T", " ")
        datetime = datetime.replace("Z", "")
        val mInputDateFormat = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
        try {
            datetime = convertServerDateToUserTimeZone(datetime)!!
            mParsedDate = mInputDateFormat.parse(datetime)
            val currentDate =
                DateFormat.format(DATE_INPUT_FORMAT, Calendar.getInstance().time)
                    .toString()
            currentParsedDate = mInputDateFormat.parse(currentDate)
            var difference = currentParsedDate.time - mParsedDate.time
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = difference / daysInMilli
            difference = difference % daysInMilli
            val elapsedHours = difference / hoursInMilli
            difference = difference % hoursInMilli
            val elapsedMinutes = difference / minutesInMilli
            difference = difference % minutesInMilli
            val elapsedSeconds = difference / secondsInMilli
            if (elapsedDays > 0) {
                return if (elapsedDays == 1L) {
                    "($elapsedDays day ago)"
                } else {
                    "($elapsedDays days ago)"
                }
            } else if (elapsedHours > 0) {
                return if (elapsedHours == 1L) {
                    "($elapsedHours hour ago)"
                } else {
                    "($elapsedHours hours ago)"
                }
            } else if (elapsedMinutes > 0) {
                return if (elapsedMinutes == 1L) {
                    "($elapsedMinutes min ago)"
                } else {
                    "($elapsedMinutes mins ago)"
                }
            } else if (elapsedSeconds > 0) {
                return if (elapsedSeconds == 1L) {
                    "($elapsedSeconds sec ago)"
                } else {
                    "($elapsedSeconds secs ago)"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return "(Just Now)"
    }

    fun convertServerDateToUserTimeZone(serverDate: String?): String? {
        var ourdate: String
        try {
            val formatter = SimpleDateFormat(DATE_INPUT_FORMAT)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(serverDate)
            val dateFormatter =
                SimpleDateFormat(DATE_INPUT_FORMAT) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourdate = dateFormatter.format(value)
            Log.d("OurDate", ourdate)
        } catch (e: java.lang.Exception) {
            ourdate = "0000-00-00 00:00:00"
        }
        return ourdate
    }

    fun getFormattedDateTime(date: String?): String? {
        var date = date
        if (date != null) {
            date = date.replace("T", " ")
            date = date.replace("Z", "")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            try {
                val serverDate = simpleDateFormat.parse(date)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                return dateFormat.format(serverDate)
            } catch (e: java.lang.Exception) {
                Log.e(">> Date Exception", e.message!!)
            }
        }
        return "--"
    }
}