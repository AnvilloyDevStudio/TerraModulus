/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

import terramodulus.util.logging.logger

private val logger = logger {}

/**
 * Records the recovered [exception] to logger.
 * This logs the `exception` as a warning.
 */
fun recordException(exception: Exception) {

}

/**
 * Pushes the recovered [exception] to application notification and records it to logger.
 * This logs the `exception` as a warning.
 */
fun notifyAndRecordException(exception: Exception) {
	// TODO exception notification
	recordException(exception)
}

/**
 * Records the suppressed [error] to logger.
 * This logs the `exception` as an error.
 */
fun recordError(error: Error) {

}

/**
 * Pushes the suppressed [error] to application notification and records it to logger.
 * This logs the `error` as an error.
 */
fun notifyAndRecordError(error: Error) {
	// TODO exception notification
	recordError(error)
}

fun triggerSessionCrash() {
	TODO()
}

fun triggerGlobalCrash(error: Error): Nothing {
	TODO()
}
