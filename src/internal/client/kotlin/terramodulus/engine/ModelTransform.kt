/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

@OptIn(ExperimentalUnsignedTypes::class)
sealed class ModelTransform(handles: ULongArray) {
	internal val handle: ULong = handles[0]
	internal val wideHandle: ULong = handles[1]
}
