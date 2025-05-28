/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

class UnhandledExceptionFault private constructor(message: String, cause: Throwable) : Fault(message, cause) {
	companion object {
		fun global(cause: Throwable) =
			UnhandledExceptionFault("Unknown unhandled exception", cause)

		fun scoped(scope: String, cause: Throwable) =
			UnhandledExceptionFault("Unknown unhandled exception in $scope", cause)
	}
}
