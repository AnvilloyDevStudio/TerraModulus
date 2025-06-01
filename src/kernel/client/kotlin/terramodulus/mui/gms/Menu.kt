/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

abstract class Menu {
	private val components = LinkedHashSet<Component>()

	abstract fun render()
}
