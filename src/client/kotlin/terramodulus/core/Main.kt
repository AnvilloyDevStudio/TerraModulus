/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import terramodulus.util.exception.UnhandledExceptionFault

fun main(args: Array<String>) {
	Thread.setDefaultUncaughtExceptionHandler { _, e ->
		// TODO find a way to add fun to commonize this part for client and server `main`
		UnhandledExceptionFault.global(e)
	}
	println("java.library.path = ${System.getProperty("java.library.path")}")

	val game = TerraModulus()
	try {
		game.run()
	} catch (e: Exception) {
		TODO("Not yet implemented")
	}
}
