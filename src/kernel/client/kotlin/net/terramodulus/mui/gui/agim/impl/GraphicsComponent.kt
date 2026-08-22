/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.AbstractPane
import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.asd.AsdHandle
import net.terramodulus.mui.gui.gfx.GeneralTransform
import net.terramodulus.mui.gui.gfx.GuiSprite
import net.terramodulus.mui.gui.gfx.RectStParams
import net.terramodulus.mui.gui.gfx.RenderSystem

sealed interface GraphicsComponent

class SpriteComponent(val sprite: GuiSprite, asdHandle: AsdHandle) : Component(asdHandle), GraphicsComponent {
	private val transform = GeneralTransform().apply { sprite.add(this) }

	init {
		val dim = IntrinsicDimensionsProperty(asdHandle.rect.width.toUInt(), asdHandle.rect.height.toUInt())
		asdHandle.properties.putProperty(IntrinsicDimensionsProperty.KEY, dim)
		asdHandle.properties.putProperty(IntrinsicRatioProperty.KEY, dim.computeRatio())
		asdHandle.observeRect {
			RectStParams.fromRects(sprite.rect.toDouble(), asdHandle.rect).applyToGeneralTransform(transform)
		}
	}

	override fun render(renderSystem: RenderSystem) {
		sprite.render(renderSystem)
	}
}

@Suppress("CanSealedSubClassBeObject")
class CanvasComponent(override val layout: Layout, asdHandle: AsdHandle) : AbstractPane(asdHandle), GraphicsComponent {
	override fun render(renderSystem: RenderSystem) {
		TODO("Not yet implemented")
	}
}
