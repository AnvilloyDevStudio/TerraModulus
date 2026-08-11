/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.gfx.AlphaFilter
import net.terramodulus.mui.gui.gfx.Dimension2I
import net.terramodulus.mui.gui.gfx.GuiRect
import net.terramodulus.mui.gui.gfx.GuiSprite
import net.terramodulus.mui.gui.gfx.RectangleI
import net.terramodulus.mui.gui.gfx.RenderSystem
import net.terramodulus.mui.gui.gfx.SmartScaling
import net.terramodulus.mui.gui.agim.Screen
import net.terramodulus.mui.gui.agim.ScreenManager
import net.terramodulus.mui.gui.agim.event.ScreenEvent
import net.terramodulus.mui.gui.asd.AsdHandle

private val REF_SIZE = Dimension2I(800, 480)

private val BG_COLOR = floatArrayOf(.145F, .776F, .768F)

private const val ANI_DURATION = .75F // in second

private const val PAUSE_DURATION = 2 // in second

internal class LaunchingScreen(
	managerHandle: ScreenManager.Handle,
	asdHandle: AsdHandle.Container,
	renderSystemHandle: RenderSystem.Handle,
) : Screen(managerHandle, asdHandle) {
	private var stage = 0
	private var last = System.currentTimeMillis() // timestamp in milliseconds
	private var alphaFilter = AlphaFilter(0F)
	override val layout = CompositeLayout(this)

	init {
		layout.update {
			add(AbsoluteLayout(this@LaunchingScreen, GeomComponent(GuiRect(
				0, 0, 1, 1, 37, 198, 196, 255
			), ComponentAsdHandleImpl()).apply {
				geom.add(alphaFilter)
			}, AbsoluteLayout.Config.Full))
		}
		layout.update {
			add(AbsoluteLayout(this@LaunchingScreen, SpriteComponent(GuiSprite(
				RectangleI(0, 0, 512, 128),
				renderSystemHandle.loadTexture("/studio_logo.png"),
			), ComponentAsdHandleImpl()).apply {
				sprite.add(alphaFilter)
				sprite.add(SmartScaling.both(REF_SIZE.width, REF_SIZE.height, 512, 128))
			}, AbsoluteLayout.Config.Full))
		}

		addListener(ScreenEvent.Update::class.java) {
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

				3 -> it.muiIoI.screenManager.handle.reset(::ResourceLoadingScreen)
			}
		}
	}
}
