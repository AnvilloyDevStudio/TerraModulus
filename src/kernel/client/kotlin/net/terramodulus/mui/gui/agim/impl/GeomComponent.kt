/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.gfx.GuiGeometry
import net.terramodulus.mui.gui.gfx.RenderSystem

class GeomComponent(val geom: GuiGeometry) : Component() {
	override fun render(renderSystem: RenderSystem) {
		geom.render(renderSystem)
	}
}
