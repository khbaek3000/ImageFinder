package com.brianbaek.imagefinder.livedata

import java.util.concurrent.atomic.AtomicBoolean

class Event<T> (val content: T){
    var hasBeenHandled: AtomicBoolean = AtomicBoolean(false)

    fun getContentIfNotHandled(): T? {
        when(hasBeenHandled.get()) {
            true -> return null
            false -> {
                hasBeenHandled.set(true)
                return content
            }
        }
    }

    fun isHandled(): Boolean {
        return hasBeenHandled.get()
    }

    fun peekContent(): T {
        return content
    }
}