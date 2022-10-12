package com.youssef.weather.framework.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

fun <VH : RecyclerView.ViewHolder> clickOnItem(
    recyclerViewId: Int,
    clickedItem: Int,
    position: Int
) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<VH>(
            position,
            clickChildViewWithId(clickedItem)
        )
    )
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}