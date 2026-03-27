/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gms.Component

/**
 * This can act as a placeholder [Component] in a [Layout][terramodulus.mui.gms.Layout].
 */
class BlankComponent : Component() {
	override fun render(renderSystem: RenderSystem) {}
}
