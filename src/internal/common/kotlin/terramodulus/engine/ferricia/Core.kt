/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine.ferricia

import terramodulus.internal.platform.Kernel32

const val NULL: Long = 0;

internal fun loadLibrary() {
	if (Kernel32.INSTANCE != null) { // For Windows
		Kernel32.INSTANCE.SetDllDirectoryW(System.getProperty("java.library.path")) // must use backslashes
	}

	System.loadLibrary("ferricia")
}
