/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gfx.AlphaFilter
import terramodulus.mui.gfx.Dimension2I
import terramodulus.mui.gfx.FullScaling
import terramodulus.mui.gfx.GuiLine
import terramodulus.mui.gfx.GuiRect
import terramodulus.mui.gfx.RectangleI
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gfx.SmartScaling
import terramodulus.mui.gms.Screen
import terramodulus.mui.gms.ScreenManager

private val REF_SIZE = Dimension2I(800, 480)

private val BG_COLOR = floatArrayOf(.145F, .776F, 0.768F)

private const val ANI_DURATION = .75F // in second

private const val PAUSE_DURATION = 1 // in second

internal class LaunchingScreen(renderSystemHandle: RenderSystem.Handle) : Screen() {
	private var alpha = 0F
	private var stage = 0
	private var last = System.currentTimeMillis() // timestamp in milliseconds
	private var alphaFilter = AlphaFilter(alpha)

	init {
		GeomComponent(GuiRect(0, 0, 800, 480, 37, 198, 196, 255), RectangleI(0, 0, 0, 0)).apply {
			geom.add(alphaFilter)
			geom.add(FullScaling(REF_SIZE))
			addComponent(this)
		}
		SpriteComponent(RectangleI(0, 0, 400, 100), renderSystemHandle.loadTexture("/logo.png")).apply {
			sprite.add(alphaFilter)
			sprite.add(SmartScaling.both(REF_SIZE.width, REF_SIZE.height, 400, 100))
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
				alpha = 1F
				alphaFilter.alpha = alpha
			} else {
				alpha = elapsed / ANI_DURATION
				alphaFilter.alpha = alpha
			}

			1 -> if (elapsed >= PAUSE_DURATION) {
				stage = 2
				last = current
			}

			2 -> if (elapsed >= ANI_DURATION) {
				stage = 3
				last = current
				alpha = 0F
				alphaFilter.alpha = alpha
			} else {
				alpha = 1 - elapsed / ANI_DURATION
				alphaFilter.alpha = alpha
			}

			3 -> screenManager.handle.reset(::ResourceLoadingScreen)
		}
	}

	override fun exit() {}
}
