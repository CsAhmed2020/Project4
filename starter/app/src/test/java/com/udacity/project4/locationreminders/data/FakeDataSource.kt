package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(val dataSource: MutableList<ReminderDTO>) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    var shouldReturnError:Boolean = false

    private lateinit var myReminder: ReminderDTO

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (shouldReturnError)
            Result.Error("Not Available reminders")
        else
            Result.Success(dataSource)
    }


    override suspend fun saveReminder(reminder: ReminderDTO) {
        dataSource.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError)
            return Result.Error("Reminder Not Found")
        else
            for (reminder in dataSource)
                if (reminder.id == id)
                    myReminder = reminder
            return Result.Success(myReminder)

    }

    override suspend fun deleteAllReminders() {
        dataSource.clear()
    }


}