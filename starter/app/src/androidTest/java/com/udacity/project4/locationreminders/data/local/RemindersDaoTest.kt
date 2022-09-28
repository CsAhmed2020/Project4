package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import junit.framework.Assert.fail

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt

    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertReminder() = runBlockingTest {
        val reminderDao = database.reminderDao()
        val reminder = ReminderDTO(
            title = "Reminder Title",
            description = "Reminder desc",
            location = "Reminder Location",
            latitude = 30.30,
            longitude = 33.30
        )
        reminderDao.saveReminder(reminder)
        val myReminder = reminderDao.getReminderById(reminder.id)
        if (myReminder !=null){
            assertThat(reminder.id, `is`(myReminder.id))
            assertThat(reminder.title, `is`(myReminder.title))
            assertThat(reminder.description, `is`(myReminder.description))
            assertThat(reminder.location, `is`(myReminder.location))
            assertThat(reminder.latitude, `is`(myReminder.latitude))
            assertThat(reminder.longitude, `is`(myReminder.longitude))
        }else
            fail()
    }

    @Test
    fun deleteAllReminders_checkEmptyList() = runBlockingTest{
        val reminderDao = database.reminderDao()
        val reminder = ReminderDTO(
            title = "Reminder Title",
            description = "Reminder desc",
            location = "Reminder Location",
            latitude = 30.30,
            longitude = 33.30
        )
        reminderDao.saveReminder(reminder)
        reminderDao.deleteAllReminders()
        val reminders = reminderDao.getReminders()
        assertThat(reminders, `is`(emptyList()))
    }

    @After
    fun closeDatabase(){
        database.close()
    }

}