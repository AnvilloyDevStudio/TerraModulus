/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import oshi.SystemInfo
import terramodulus.engine.ferricia.Demo
import terramodulus.internal.platform.Kernel32
import java.util.Locale

fun main() {
	println("java.library.path = ${System.getProperty("java.library.path")}")
	if (SystemInfo().operatingSystem.family.lowercase(Locale.getDefault()).contains("windows")) {
		Kernel32.INSTANCE.SetDllDirectoryW(System.getProperty("java.library.path")) // must use backslashes
	}
	System.loadLibrary("ferricia")
	println(Demo.hello("Ferricia"))
	println(Demo.clientOnly())
	val game = TerraModulus()
	try {
		game.run()
	} catch (e: Exception) {
		TODO("Not yet implemented")
	}
}
