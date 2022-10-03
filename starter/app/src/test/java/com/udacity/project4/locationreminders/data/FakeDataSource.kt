package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(val dataSource: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    var shouldReturnError:Boolean = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
         if (shouldReturnError) {
            return Result.Error("No reminders Exception")
        }else{
            dataSource?.let { return Result.Success(ArrayList(it)) }
            return Result.Error(Exception("Not Available reminders").toString())
        }

    }


    override suspend fun saveReminder(reminder: ReminderDTO) {
        dataSource?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error("Reminder not found Exception")
        }
        else{
            dataSource?.filter {
                it.id == id
            }?.let {
                return Result.Success(it.first())
            }
            return Result.Error("Reminder not found!")
        }

    }

    override suspend fun deleteAllReminders() {
        dataSource?.clear()
    }


}