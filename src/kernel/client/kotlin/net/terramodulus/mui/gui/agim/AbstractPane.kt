/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

abstract class AbstractPane : Component(), Container {
	final override fun update(muiIopIf: ScreenManager.MuiIopIf) {
		super.update(muiIopIf)
		layout.update()
		layout.components.forEach { it.update(muiIopIf) }
	}
}
