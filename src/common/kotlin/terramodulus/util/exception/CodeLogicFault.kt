/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

/**
 * Code logic problems or bugs are regarded as defects in the software code and thus
 * should be emitted as [Fault]s.
 *
 * @param cause accepts [IllegalArgumentException], [IllegalStateException], [IndexOutOfBoundsException],
 * [ArithmeticException], [NumberFormatException], [NullPointerException], [AssertionError],
 * [ClassCastException], [NoSuchElementException], [ConcurrentModificationException]
 */
class CodeLogicFault(override val message: String, override val cause: Throwable?) : Fault(message, cause) {
	constructor(message: String) : this(message, null)
}
