/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gfx.RectangleI
import terramodulus.mui.gfx.RenderSystem

abstract class Component {
	abstract var rect: RectangleI

	abstract fun render(renderSystem: RenderSystem)
}
