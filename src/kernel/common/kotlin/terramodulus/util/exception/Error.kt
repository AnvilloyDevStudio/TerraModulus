/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

abstract class Error(override val message: String, override val cause: Throwable?) : Exception(message, cause) {
	constructor(message: String) : this(message, null)
}
