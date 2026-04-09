/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms.impl

import net.terramodulus.mui.gfx.AlphaFilter
import net.terramodulus.mui.gfx.Dimension2I
import net.terramodulus.mui.gfx.FullScaling
import net.terramodulus.mui.gfx.GuiRect
import net.terramodulus.mui.gfx.GuiSprite
import net.terramodulus.mui.gfx.RectangleI
import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gfx.SmartScaling
import net.terramodulus.mui.gms.Screen
import net.terramodulus.mui.gms.ScreenManager

private val REF_SIZE = Dimension2I(800, 480)

private val BG_COLOR = floatArrayOf(.145F, .776F, .768F)

private const val ANI_DURATION = .75F // in second

private const val PAUSE_DURATION = 1 // in second

internal class LaunchingScreen(renderSystemHandle: RenderSystem.Handle) : Screen() {
	private var stage = 0
	private var last = System.currentTimeMillis() // timestamp in milliseconds
	private var alphaFilter = AlphaFilter(0F)

	init {
		GeomComponent(GuiRect(0, 0, 800, 480, 37, 198, 196, 255)).apply {
			geom.add(alphaFilter)
			geom.add(FullScaling(REF_SIZE))
			addComponent(this)
		}
		SpriteComponent(GuiSprite(
			RectangleI(0, 0, 300, 300),
			renderSystemHandle.loadTexture("/studio_logo.png"),
		)).apply {
			sprite.add(alphaFilter)
			sprite.add(SmartScaling.both(REF_SIZE.width, REF_SIZE.height, 300, 300))
			addComponent(this)
		}
	}

	override fun update(renderSystem: RenderSystem, screenManager: ScreenManager) {
		val current = System.currentTimeMillis()
		val elapsed = (current - last) / 1000F // elapsed time for this stage
		when (stage) {
			0 -> if (elapsed >= ANI_DURATION) {
				stage = 1
				last = current
				alphaFilter.alpha = 1F
			} else {
				alphaFilter.alpha = elapsed / ANI_DURATION
			}

			1 -> if (elapsed >= PAUSE_DURATION) {
				stage = 2
				last = current
			}

			2 -> if (elapsed >= ANI_DURATION) {
				stage = 3
				last = current
				alphaFilter.alpha = 0F
			} else {
				alphaFilter.alpha = 1 - elapsed / ANI_DURATION
			}

			3 -> screenManager.handle.reset(::ResourceLoadingScreen)
		}
	}

	override fun exit() {}
}
