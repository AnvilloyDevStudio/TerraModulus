/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import terramodulus.common.core.setupInit

fun main() {
	setupInit()
	println("java.library.path = ${System.getProperty("java.library.path")}")

}
