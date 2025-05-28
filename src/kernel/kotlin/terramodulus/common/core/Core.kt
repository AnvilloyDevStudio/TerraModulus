/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.common.core

import terramodulus.util.exception.UnhandledExceptionFault

/**
 * This should only be used by `terramodulus.core.Main` in the beginning of `main` function.
 */
fun setupInit() {
	Thread.setDefaultUncaughtExceptionHandler { _, e ->
		UnhandledExceptionFault.global(e)
	}
}
