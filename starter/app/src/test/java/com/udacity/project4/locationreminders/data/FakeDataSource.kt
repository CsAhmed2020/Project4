package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(val dataSource: MutableList<ReminderDTO>) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    var resultSuccess:Boolean = true

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (resultSuccess)
            Result.Success(dataSource)
        else
            Result.Error("Not Available reminders")
    }


    override suspend fun saveReminder(reminder: ReminderDTO) {
        dataSource.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        for (reminder in dataSource){
            if (reminder.id == id)
                return Result.Success(reminder)
        }
        return Result.Error("Reminder Not Found")
    }

    override suspend fun deleteAllReminders() {
        dataSource.clear()
    }


}