package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        stopKoin()
        val fakeReminders = mutableListOf<ReminderDTO>()
        fakeReminders.addAll(
            listOf(
                ReminderDTO(
                    title = "Reminder Title 1",
                    description = "Reminder Desc 1",
                    location = "Reminder Location 1",
                    latitude =31.28,
                    longitude = 31.31
                ),
                ReminderDTO(
                    title = "Reminder Title 2",
                    description = "Reminder Desc 2",
                    location = "Reminder Location 2",
                    latitude =30.28,
                    longitude = 30.31
                ),
                ReminderDTO(
                    title = "Reminder Title 3",
                    description = "Reminder Desc 3",
                    location = "Reminder Location 3",
                    latitude =33.28,
                    longitude = 33.31
                )
            )
        )
        fakeDataSource = FakeDataSource(fakeReminders)
        remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

    }

    @Test
    fun loadReminders_checkNotNull(){
        remindersListViewModel.loadReminders()
        val reminders = remindersListViewModel.remindersList.getOrAwaitValue()
        assertThat(reminders, not(nullValue()))
    }


    @Test
    fun loadReminders_checkLoadingState(){
        mainCoroutineRule.pauseDispatcher()
        remindersListViewModel.loadReminders()
        assertThat(
            remindersListViewModel.showLoading.getOrAwaitValue(),
            `is`(true)
        )
        mainCoroutineRule.resumeDispatcher()
        assertThat(
            remindersListViewModel.showLoading.getOrAwaitValue(),
            `is`(false)
        )

    }

    @Test
    fun loadReminders_checkErrorState(){
        fakeDataSource.shouldReturnError = true
        remindersListViewModel.loadReminders()
        assertThat(remindersListViewModel.showSnackBar.getOrAwaitValue(),`is`("get reminders Error Exception"))
    }

}