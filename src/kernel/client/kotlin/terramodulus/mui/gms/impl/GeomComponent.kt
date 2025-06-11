/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gfx.GuiGeometry
import terramodulus.mui.gfx.RectangleI
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.Component

class GeomComponent(val geom: GuiGeometry, override var rect: RectangleI) : Component() {
	override fun render(renderSystem: RenderSystem) {
		geom.render(renderSystem)
	}
}
