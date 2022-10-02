package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
     private val reminder = ReminderDTO(
         title = "Reminder Title",
         description = "Reminder desc",
         location = "Reminder Location",
         latitude = 30.30,
         longitude = 33.30
     )

    @Before
    fun initDatabase(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = RemindersLocalRepository(database.reminderDao(),Dispatchers.Main)

    }

    @Test
    fun saveReminder_checkEqualToRepository() = runBlocking{
        repository.saveReminder(reminder)

        val myReminder = (repository.getReminder(reminder.id) as Result.Success).data

        assertThat(myReminder.id, `is`(reminder.id))
        assertThat(myReminder.title, `is`(reminder.title))
        assertThat(myReminder.description, `is`(reminder.description))
        assertThat(myReminder.location, `is`(reminder.location))
        assertThat(myReminder.latitude, `is`(reminder.latitude))
        assertThat(myReminder.longitude, `is`(reminder.longitude))
    }

    @Test
    fun deleteAllReminders_CheckEmptyList() = runBlocking{
        repository.saveReminder(reminder)
        repository.deleteAllReminders()
        val remindersFromRepository = (repository.getReminders() as Result.Success).data
        assertThat(remindersFromRepository, `is`(emptyList()))
    }

    @Test
    fun getReminder_returnNotFound()= runBlocking{
        repository.saveReminder(reminder)
        repository.deleteAllReminders()
        val remindersFromRepository = (repository.getReminder(reminder.id) as Result.Error).message
        assertThat(remindersFromRepository, `is`("Reminder not found!"))

    }

    @After
    fun closeDatabase(){
        database.close()
    }

}