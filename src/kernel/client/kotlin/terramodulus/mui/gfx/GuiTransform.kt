/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.ModelTransform
import terramodulus.engine.SmartScaling

class GuiTransform private constructor(private val handle: ModelTransform) {
	companion object {
		fun smartScaling(w: Int, h: Int) = GuiTransform(SmartScaling(w, h))
	}

	fun add
}
