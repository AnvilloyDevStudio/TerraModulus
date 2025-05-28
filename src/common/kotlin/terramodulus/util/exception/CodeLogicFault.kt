/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.exception

/**
 * Code logic problems or bugs are regarded as defects in the software code and thus
 * should be emitted as [Fault]s. This alerts that a made assumption or assertion was false.
 *
 * @param cause generally accepts [IllegalArgumentException], [IllegalStateException],
 * [IndexOutOfBoundsException], [ArithmeticException], [NumberFormatException],
 * [NullPointerException], [AssertionError], [ClassCastException], [NoSuchElementException],
 * [ConcurrentModificationException], [ArrayStoreException], etc.
 */
class CodeLogicFault(cause: Throwable) : Fault("Illogical code", cause)
