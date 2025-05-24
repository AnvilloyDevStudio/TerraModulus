/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.logging

internal object State {
	fun appender() {
		logger {}.info { "Appender" }
	}

	fun core() {
		logger {}.info { "Core" }
	}
}
