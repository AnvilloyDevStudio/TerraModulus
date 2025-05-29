/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import terramodulus.common.core.run
import terramodulus.common.core.setupInit

fun main(args: Array<String>) {
	setupInit()
	parseArgs(args)
	println("java.library.path = ${System.getProperty("java.library.path")}")
	run(TerraModulus())
}

fun parseArgs(args: Array<String>) {

}
