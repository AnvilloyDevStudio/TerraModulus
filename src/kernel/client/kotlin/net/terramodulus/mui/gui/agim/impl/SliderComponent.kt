/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.gfx.Anchor5
import net.terramodulus.mui.gui.gfx.Direction4A
import net.terramodulus.mui.gui.gfx.Direction4AD
import net.terramodulus.mui.gui.gfx.GuiRect
import net.terramodulus.mui.gui.gfx.Rectangle
import net.terramodulus.mui.gui.gfx.RectangleF
import net.terramodulus.mui.gui.gfx.RenderSystem
import kotlin.properties.Delegates

class SliderComponent(val dir: Direction4A) : Component() {
	private var bgInit = false
	private var fgInit = false
	private lateinit var background: GuiRect
	private lateinit var bgComponent: GeomComponent
	private lateinit var foreground: GuiRect
	private lateinit var fgComponent: GeomComponent
	var fraction: Float by Delegates.observable(0F) { _, _, value ->
		updateForeground(rect.value, value)
	}

	init {
		rect.observe {
			val anchor = it.anchor(Anchor5.TopRight)
			if (!bgInit) {
				background = GuiRect(it.x.toInt(), it.y.toInt(), anchor.xi, anchor.yi, 255, 255, 255, 255)
				bgComponent = GeomComponent(background)
				bgInit = true
			} else {
				background.setPos(it.x.toInt(), it.y.toInt(), anchor.xi, anchor.yi)
			}
			updateForeground(it, fraction)
		}
	}

	private fun updateForeground(bounds: RectangleF, fraction: Float) {
		// (bounds.x, bounds.y) is the bottom-left anchor
		val topRight = bounds.anchor(Anchor5.TopRight)
		val rect = when (dir) {
			Direction4A.XPos -> // left to right
				Rectangle.withDirection(bounds.x, bounds.y, bounds.width * fraction, bounds.height, Direction4AD.QuadOne)
			Direction4A.XNeg -> // right to left
				Rectangle.withDirection(topRight.x, bounds.y, bounds.width * fraction, bounds.height, Direction4AD.QuadTwo)
			Direction4A.YPos -> // bottom to top
				Rectangle.withDirection(bounds.x, bounds.y, bounds.width, bounds.height * fraction, Direction4AD.QuadOne)
			Direction4A.YNeg -> // top to bottom
				Rectangle.withDirection(bounds.x, topRight.y, bounds.width, bounds.height * fraction, Direction4AD.QuadFour)
		}
		val anchor = rect.anchor(Anchor5.TopRight)
		if (!fgInit) {
			foreground = GuiRect(rect.x.toInt(), rect.y.toInt(), anchor.xi, anchor.yi, 122, 122, 122, 255)
			fgComponent = GeomComponent(foreground)
			fgInit = true
		} else {
			foreground.setPos(rect.x.toInt(), rect.y.toInt(), anchor.xi, anchor.yi)
		}
	}

	override fun render(renderSystem: RenderSystem) {
		rect.value
		bgComponent.render(renderSystem)
		fgComponent.render(renderSystem)
	}
}
