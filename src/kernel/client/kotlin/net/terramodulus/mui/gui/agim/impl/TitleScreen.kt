/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.gfx.RenderSystem
import net.terramodulus.mui.gui.agim.Screen
import net.terramodulus.mui.gui.agim.ScreenManager

class TitleScreen(
	renderSystemHandle: RenderSystem.Handle,
	managerHandle: ScreenManager.Handle,
	rect: ScreenManager.DelegatedRect,
) : Screen(managerHandle, rect) {
}
