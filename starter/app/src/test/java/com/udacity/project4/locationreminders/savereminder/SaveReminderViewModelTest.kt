package com.udacity.project4.locationreminders.savereminder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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
        saveReminderViewModel = SaveReminderViewModel(getApplicationContext(),fakeDataSource)

    }

    @SuppressLint("CheckResult")
    @Test
    fun onClear_returnObjectsIsNull(){
        saveReminderViewModel.onClear()

        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(),nullValue())
        assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(),nullValue())
        assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(),nullValue())
        assertThat(saveReminderViewModel.selectedPOI.getOrAwaitValue(),nullValue())
        assertThat(saveReminderViewModel.latitude.getOrAwaitValue(),nullValue())
        assertThat(saveReminderViewModel.longitude.getOrAwaitValue(),nullValue())
    }

    @Test
    fun saveReminder_equalToDataSource()= mainCoroutineRule.runBlockingTest {
        fakeDataSource.deleteAllReminders()
        val reminder = ReminderDataItem(
            "ReminderTitle",
            "reminder Desc",
            "Reminder Location",
            20.20,
            50.20
        )

        saveReminderViewModel.saveReminder(reminder)
        val reminderFromDataSource = (fakeDataSource.getReminders() as Result.Success).data
        assertThat(reminder.title, equalTo(reminderFromDataSource[0].title))
    }

    @Test
    fun saveReminder_ShowToast(){
        val reminder = ReminderDataItem(
            "ReminderTitle",
            "reminder Desc",
            "Reminder Location",
            20.20,
            50.20
        )
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), `is`(getApplicationContext<Context>().getString(
            R.string.reminder_saved)))
    }
    @Test
    fun saveReminder_checkLoadingState(){
        val reminder = ReminderDataItem(
            "ReminderTitle",
            "reminder Desc",
            "Reminder Location",
            20.20,
            50.20
        )
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))

    }

}