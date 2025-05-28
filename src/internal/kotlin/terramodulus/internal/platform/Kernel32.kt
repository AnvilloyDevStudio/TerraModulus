/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.internal.platform

import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.platform.win32.WinBase
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

@Suppress("FunctionName")
interface Kernel32 : com.sun.jna.platform.win32.Kernel32, StdCallLibrary, WinBase {
	// BOOL SetDllDirectoryW(LPCWSTR lpPathName);
	fun SetDllDirectoryW(lpPathName: String?): Boolean

	companion object {
		val INSTANCE: Kernel32? =
			if (Platform.isWindows()) Native.load("kernel32", Kernel32::class.java, W32APIOptions.DEFAULT_OPTIONS)
			else null; // without relying on classloading
	}
}
