/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.engine.common.ZeroImmVec3f
import net.terramodulus.mui.gui.agim.Screen
import net.terramodulus.mui.gui.agim.ScreenManager
import net.terramodulus.mui.gui.gfx.AlphaFilter
import net.terramodulus.mui.gui.gfx.Dimension2I
import net.terramodulus.mui.gui.gfx.FullScaling
import net.terramodulus.mui.gui.gfx.GuiRect
import net.terramodulus.mui.gui.gfx.GuiSprite
import net.terramodulus.mui.gui.gfx.Rectangle
import net.terramodulus.mui.gui.gfx.RectangleI
import net.terramodulus.mui.gui.gfx.RenderSystem
import net.terramodulus.mui.gui.gfx.SmartScaling
import net.terramodulus.mui.kui.InputSystem
import kotlin.math.min
import kotlin.properties.Delegates

private val REF_SIZE = Dimension2I(800, 480)

private val CONTENT_SIZE = Dimension2I(400, 200)

private val BG_COLOR = floatArrayOf(.145F, .776F, 0.768F)

private const val ANI_DURATION = 1F // in second
private const val PAUSE_DURATION = 2F // in second

class ResourceLoadingScreen(
	renderSystemHandle: RenderSystem.Handle,
	managerHandle: ScreenManager.Handle,
	rect: ScreenManager.DelegatedRect,
) : Screen(managerHandle, rect) {
	private var stage = 0
	private var last = System.currentTimeMillis() // timestamp in milliseconds
	private var alphaFilter = AlphaFilter(0F)
	private val progressBar = ProgressBar()

	init {
		GeomComponent(GuiRect(0, 0, 800, 480, 0, 255, 213, 255)).apply {
			geom.add(alphaFilter)
			geom.add(FullScaling(REF_SIZE))
			addComponent(this)
		}
		val smartScaling = SmartScaling.both(REF_SIZE.width, REF_SIZE.height, CONTENT_SIZE.width, CONTENT_SIZE.height)
		SpriteComponent(GuiSprite(RectangleI(0, 100, 400, 100), renderSystemHandle.loadTexture("/game_logo.png"))).apply {
			sprite.add(alphaFilter)
			sprite.add(smartScaling)
			addComponent(this)
		}
		GeomComponent(GuiRect(0, 0, 400, 40, 240, 240, 240, 255)).apply {
			geom.add(alphaFilter)
			geom.add(smartScaling)
			addComponent(this)
		}
		GeomComponent(GuiRect(5, 5, 395, 35, 0, 255, 213, 255)).apply {
			geom.add(alphaFilter)
			geom.add(smartScaling)
			addComponent(this)
		}
		GeomComponent(progressBar.rect).apply {
			geom.add(alphaFilter)
			geom.add(smartScaling)
			addComponent(this)
		}
	}

	private class ProgressBar {
		val rectDim = Rectangle.withPoints(7, 7, 393, 33)
		val length = rectDim.width
		var progress: Float by Delegates.observable(0f) { _, _, _ ->
			rect.setPos(7, 7, rectDim.x + (progress * length).toInt(), 33)
		}
		val rect = GuiRect(7, 7, 7, 33, 240, 240, 240, 255)
	}

	override fun update(renderSystem: RenderSystem, screenManager: ScreenManager, inputSystem: InputSystem) {
		val current = System.currentTimeMillis()
		val elapsed = (current - last) / 1000F // elapsed time in second at this stage
		when (stage) {
			0 -> if (elapsed >= ANI_DURATION) {
				stage = 1
				last = current
				alphaFilter.alpha = 1F
			} else {
				alphaFilter.alpha = elapsed / ANI_DURATION
			}

			1 -> {
				// TODO when there is something to load, stay at this stage until ready
				progressBar.progress = min(elapsed / PAUSE_DURATION, 1F)
				if (progressBar.progress >= 1F) {
					stage = 2
					last = current
				}
			}

			2 -> if (elapsed >= ANI_DURATION) {
				stage = 3
				last = current
				alphaFilter.alpha = 0F
			} else {
				alphaFilter.alpha = 1 - elapsed / ANI_DURATION
			}

// 			3 -> screenManager.handle.openBefore(::TitleScreen, this)
			3 -> screenManager.handle.reset(renderSystem.newGameplayScreen(ZeroImmVec3f))
		}
	}
}
