/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.SpriteMesh

class GuiSprite(private val rect: RectangleI, private val texture: UInt) {
	private val mesh = SpriteMesh(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height)

	fun smartScaling(referX: Int, referY: Int) = SmartScaling.both(referX, referY, rect.width, rect.height)

	fun alpha(alpha: Float) = AlphaFilter(alpha)

	fun add(model: ModelTransform) = mesh.add(model)

	fun add(filter: ColorFilter) = mesh.add(filter)

	fun render(renderSystem: RenderSystem) {
		renderSystem.renderGuiTex(mesh, texture)
	}
}
