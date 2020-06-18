package me.jacoblewis.network.utils

import java.util.concurrent.Callable

interface BaseCallable<T> : Callable<T> {
    fun setDataAfterLoading(data: T?)
}