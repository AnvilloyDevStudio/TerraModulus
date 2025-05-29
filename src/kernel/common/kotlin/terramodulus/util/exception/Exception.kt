/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

abstract class Exception(override val message: String, override val cause: Throwable?) : Throwable(message, cause) {
	constructor(message: String) : this(message, null)
}
