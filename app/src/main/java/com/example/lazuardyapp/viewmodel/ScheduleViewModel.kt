package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.data.model.ActivePackage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.TemporalAdjusters

data class ScheduleItem(
    val id: Int,
    val subject: String,
    val tutor: String,
    val date: String,
    val time: String,
    val status: ScheduleStatus = ScheduleStatus.SCHEDULED
)

enum class ScheduleStatus {
    SCHEDULED, COMPLETED, CANCELED
}

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val gson = Gson()
    var activePackageList by mutableStateOf<List<ActivePackage>>(emptyList())
    var scheduleList by mutableStateOf<List<ScheduleItem>>(emptyList())
    var isLoading by mutableStateOf(true)

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        isLoading = true
        viewModelScope.launch {
            sessionManager.getActivePackageJson().collectLatest { jsonString ->

                val listType = object : TypeToken<List<ActivePackage>>() {}.type

                val loadedList: List<ActivePackage> = if (jsonString.isNullOrEmpty()) {
                    emptyList()
                } else {
                    try {
                        gson.fromJson(jsonString, listType) ?: emptyList()
                    } catch (e: Exception) {
                        emptyList()
                    }
                }

                activePackageList = loadedList

                scheduleList = generateCombinedSchedule(loadedList)

                isLoading = false
            }
        }
    }

    private fun generateCombinedSchedule(packages: List<ActivePackage>): List<ScheduleItem> {
        val combinedList = mutableListOf<ScheduleItem>()
        var globalSessionId = 0

        for (pkg in packages) {
            val packageSchedule = generateMockScheduleForPackage(pkg, globalSessionId)
            combinedList.addAll(packageSchedule)
            globalSessionId += pkg.totalSessions
        }

        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
        return combinedList.sortedBy {
            try {
                LocalDate.parse(it.date, dateFormatter)
            } catch (e: Exception) {
                LocalDate.MAX
            }
        }
    }

    private fun generateMockScheduleForPackage(pkg: ActivePackage, startId: Int): List<ScheduleItem> {
        val totalSessions = pkg.totalSessions
        val progress = pkg.currentProgress

        val selectedDaysOfWeek = pkg.scheduleDays.mapNotNull { dayName ->
            when (dayName.lowercase(Locale.ROOT)) {
                "senin" -> DayOfWeek.MONDAY
                "selasa" -> DayOfWeek.TUESDAY
                "rabu" -> DayOfWeek.WEDNESDAY
                "kamis" -> DayOfWeek.THURSDAY
                "jumat" -> DayOfWeek.FRIDAY
                "sabtu" -> DayOfWeek.SATURDAY
                "minggu" -> DayOfWeek.SUNDAY
                else -> null
            }
        }.sortedBy { it.value }

        if (selectedDaysOfWeek.isEmpty()) return emptyList()

        val list = mutableListOf<ScheduleItem>()
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))

        for (i in 1..progress) {
            list.add(
                ScheduleItem(
                    id = startId + i,
                    subject = pkg.subject,
                    tutor = pkg.tutorName,
                    date = LocalDate.now().minusWeeks((progress - i + 1).toLong()).format(dateFormatter),
                    time = "14:00 - 15:30",
                    status = ScheduleStatus.COMPLETED
                )
            )
        }

        var sessionsAdded = 0
        var currentDate = LocalDate.now().plusDays(1)
        var dayIndex = 0

        while (sessionsAdded < (totalSessions - progress)) {
            val targetDay = selectedDaysOfWeek[dayIndex % selectedDaysOfWeek.size]

            currentDate = currentDate.with(TemporalAdjusters.nextOrSame(targetDay))

            if (currentDate.isBefore(LocalDate.now().plusDays(1)) && sessionsAdded == 0) {
                currentDate = currentDate.with(TemporalAdjusters.next(targetDay))
            }


            list.add(
                ScheduleItem(
                    id = startId + progress + sessionsAdded + 1,
                    subject = pkg.subject,
                    tutor = pkg.tutorName,
                    date = currentDate.format(dateFormatter),
                    time = "16:00 - 17:30",
                    status = ScheduleStatus.SCHEDULED
                )
            )

            sessionsAdded++
            dayIndex++

            currentDate = currentDate.plusDays(1)
        }

        return list
    }
}