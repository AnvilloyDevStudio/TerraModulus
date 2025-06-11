/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.Level as KLevel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.status.StatusData
import org.apache.logging.log4j.status.StatusListener
import org.apache.logging.log4j.status.StatusLogger

fun logger(func: () -> Unit): KLogger = KotlinLogging.logger(func)

internal fun initLogging() {
	System.setProperty("log4j2.debug", "true")
	StatusLogger.getLogger().registerListener(object : StatusListener {
		override fun close() {}

		override fun log(data: StatusData) {
			logger.at(when (data.level) {
				Level.FATAL -> KLevel.ERROR
				Level.ERROR -> KLevel.ERROR
				Level.WARN -> KLevel.WARN
				Level.INFO -> KLevel.INFO
				Level.DEBUG -> KLevel.DEBUG
				Level.TRACE -> KLevel.TRACE
				else -> throw IllegalArgumentException("Unsupported log level: ${data.level}")
			}) {
				cause = data.throwable
				message = data.message.formattedMessage
			}
		}

		override fun getStatusLevel() = Level.ALL
	})
}

private val logger = logger {}
