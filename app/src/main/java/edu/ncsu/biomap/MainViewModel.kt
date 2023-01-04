package org.kodein.di.samples.edu.ncsu.biomap

import android.content.Context
import android.os.Bundle
import edu.ncsu.biomap.DefaultMainNavigator
import edu.ncsu.biomap.MainNavigator
import edu.ncsu.biomap.R
import edu.ncsu.biomap.SupportedUserType

/**
 * @author Michael Roebuck
 * The main view model interface is exclusively assigned to the main view, and provides
 * all of the functionality and input required for that screen to behave properly.
 */
interface MainViewModel {
    /**
     * @author Michael Roebuck
     * This list contains the resource IDs of all of the types of users who are currently supported.
     */
    val usersResIds: List<SupportedUserType>
    fun emit(context: Context, type: SupportedUserType)
}

