package com.example.news_core

import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher



/**
 * Коре модуль созданный для утилит. Для удобства. Возможность работать с корутинами там, где необходимо.
 */

class AppDispatcher(
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
)
