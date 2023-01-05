package edu.ncsu.biomap

import android.app.Activity
import android.app.Application
import edu.ncsu.biomap.user.*
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindings.WeakContextScope

class BioMapApplication : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@BioMapApplication))

        bindProvider<UserInterestInteractor> { DefaultUserInterestInteractor() }
        bindSingleton<UserInterestNavigator> { DefaultUserInterestNavigator() }

        // this is bound in the scope of an activity so any retrieval using the same activity will return the same Kettle instance
        bind<UserInterestViewModel>() with scoped(WeakContextScope.of<Activity>()).singleton { DefaultUserInterestViewModel(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        val k = di
        println(k)
    }
}