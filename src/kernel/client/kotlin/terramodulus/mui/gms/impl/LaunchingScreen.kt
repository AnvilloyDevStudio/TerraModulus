/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gfx.RectangleI
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.Screen

internal class LaunchingScreen(renderSystemHandle: RenderSystem.Handle) : Screen() {
	init {
		addComponent(SpriteComponent(RectangleI(0, 0, 400, 100), renderSystemHandle.loadTexture("/test.png")))
	}

	override fun exit() {
		TODO("Not yet implemented")
	}
}
