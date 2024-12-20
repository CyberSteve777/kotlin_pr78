package me.cybersteve.kotlin_pr78

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("me.cybersteve.kotlin_pr78", appContext.packageName)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkUiElementsVisibility() {
        onView(withId(R.id.button)).check(matches(isDisplayed()))
        onView(withId(R.id.txt)).check(matches(isDisplayed()))
    }

    @Test
    fun checkImageInitialState() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkNotNull(R.id.imageView).inv()
        }, 2000)
    }

    @Test
    fun checkText() {
        onView(withId(R.id.txt)).check(matches(withText("There will be image above")))
    }

    @Test
    fun checkButtonClick() {
        onView(withId(R.id.button)).perform(click())
        Handler(Looper.getMainLooper()).postDelayed({
            checkNotNull(R.id.imageView)
        }, 2000)
    }

    @Test
    fun checkTextChangedDisplayAfterDownload() {
        Handler(Looper.getMainLooper()).postDelayed({
            onView(withId(R.id.button)).perform(click())
            onView(withId(R.id.txt)).check(matches(withText("Capaci-cone")))
            onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        }, 3000)
    }
}