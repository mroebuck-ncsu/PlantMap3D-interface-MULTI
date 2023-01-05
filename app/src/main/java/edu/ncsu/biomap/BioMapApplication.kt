package edu.ncsu.biomap

import android.app.Activity
import android.app.Application
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindings.WeakContextScope

class BioMapApplication : Application(), DIAware {

    // TODO: We'll inject this into each activity later...
    override val di by DI.lazy {
        import(androidXModule(this@BioMapApplication))

//        bindProvider<MainInteractor> { DefaultUserInterestInteractor() }
        bindSingleton<MainNavigator> { DefaultMainNavigator() }

        // this is bound in the scope of an activity so any retrieval using the same activity will return the same Kettle instance
        bind<MainViewModel>() with scoped(WeakContextScope.of<Activity>()).singleton { DefaultMainViewModel(instance()) }
    }
    // END TODO

    override fun onCreate() {
        super.onCreate()
        val k = di
        println(k)
    }
}