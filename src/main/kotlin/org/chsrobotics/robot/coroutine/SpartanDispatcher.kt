package org.chsrobotics.robot.coroutine

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val SpartanDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
