/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine.ferricia

import terramodulus.internal.platform.Kernel32

fun loadLibrary() {
	if (Kernel32.INSTANCE != null) {
		Kernel32.INSTANCE.SetDllDirectoryW(System.getProperty("java.library.path")) // must use backslashes
	}
	System.loadLibrary("ferricia")

}
