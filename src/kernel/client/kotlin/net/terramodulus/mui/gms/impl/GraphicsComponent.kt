/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms.impl

import net.terramodulus.mui.gfx.GuiSprite
import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gms.AbstractPanel
import net.terramodulus.mui.gms.Component

sealed interface GraphicsComponent

class SpriteComponent(val sprite: GuiSprite) : Component(), GraphicsComponent {
	override fun render(renderSystem: RenderSystem) {
		sprite.render(renderSystem)
	}
}

@Suppress("CanSealedSubClassBeObject")
class CanvasComponent : AbstractPanel(), GraphicsComponent {
	override fun render(renderSystem: RenderSystem) {
		TODO("Not yet implemented")
	}
}
