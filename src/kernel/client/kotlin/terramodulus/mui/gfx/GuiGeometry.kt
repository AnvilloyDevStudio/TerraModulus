/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.GeomDrawable
import terramodulus.engine.SimpleLineGeom
import terramodulus.engine.SimpleRectGeom

sealed class GuiGeometry(protected val geom: GeomDrawable) {
	fun add(model: ModelTransform) = geom.add(model)

	fun add(filter: ColorFilter) = geom.add(filter)

	fun render(renderSystem: RenderSystem) = renderSystem.renderGuiGeo(geom)
}

class GuiLine(x0: Int, y0: Int, x1: Int, y1: Int, r: Int, g: Int, b: Int, a: Int) :
	GuiGeometry(SimpleLineGeom(x0, y0, x1, y1, r, g, b, a))

class GuiRect(x0: Int, y0: Int, x1: Int, y1: Int, r: Int, g: Int, b: Int, a: Int) :
	GuiGeometry(SimpleRectGeom(x0, y0, x1, y1, r, g, b, a))
