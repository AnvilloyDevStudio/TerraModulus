/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gfx.GuiSprite
import terramodulus.mui.gfx.RectangleI
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.Component

class SpriteComponent(override var rect: RectangleI, texture: UInt) : Component() {
	private val sprite = GuiSprite(rect, texture)
	private var tick = 0;
	private val alpha = sprite.alpha(0F);

	init {
		sprite.add(sprite.smartScaling(800, 480))
		sprite.add(alpha)
	}

	override fun render(renderSystem: RenderSystem) {
		sprite.render(renderSystem)
		tick = (tick + 1) % 50
		alpha.alpha = tick / 50F
	}
}
