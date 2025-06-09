/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gms.Screen

internal class LaunchingScreen : Screen() {
	init {
		addComponent(SpriteComponent())
	}

	override fun exit() {
		TODO("Not yet implemented")
	}
}
