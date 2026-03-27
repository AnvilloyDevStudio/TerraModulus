/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms.impl

import net.terramodulus.mui.gfx.GuiGeometry
import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gms.Component

class GeomComponent(val geom: GuiGeometry) : Component() {
	override fun render(renderSystem: RenderSystem) {
		geom.render(renderSystem)
	}
}
