/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

fun main(args: Array<String>) {
	println("java.library.path = ${System.getProperty("java.library.path")}")

	val game = TerraModulus()
	try {
		game.run()
	} catch (e: Exception) {
		TODO("Not yet implemented")
	}
}
