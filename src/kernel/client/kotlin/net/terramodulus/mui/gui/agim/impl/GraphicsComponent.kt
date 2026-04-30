/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.AbstractPane
import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.gfx.GuiSprite
import net.terramodulus.mui.gui.gfx.RenderSystem

sealed interface GraphicsComponent

class SpriteComponent(val sprite: GuiSprite) : Component(), GraphicsComponent {
	override fun render(renderSystem: RenderSystem) {
		sprite.render(renderSystem)
	}
}

@Suppress("CanSealedSubClassBeObject")
class CanvasComponent(override val layout: Layout) : AbstractPane(), GraphicsComponent {
	override fun render(renderSystem: RenderSystem) {
		TODO("Not yet implemented")
	}
}
