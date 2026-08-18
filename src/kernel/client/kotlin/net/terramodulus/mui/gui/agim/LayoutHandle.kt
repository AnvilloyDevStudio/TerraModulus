/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.asd.AsdHandle

sealed interface LayoutHandle {
	fun getUnit(handle: AsdHandle): Unit

	sealed interface Unit {
		val properties: Map<AgimoPropertyMap.Key<out AgimoProperty>, AgimoProperty>
	}
}
