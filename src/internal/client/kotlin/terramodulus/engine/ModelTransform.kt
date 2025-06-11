/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.modelFullScaling
import terramodulus.engine.ferricia.Mui.modelSmartScaling

@OptIn(ExperimentalUnsignedTypes::class)
sealed class ModelTransform(handles: ULongArray) {
	internal val handle: ULong = handles[0]
	internal val wideHandle: ULong = handles[1]
}

@OptIn(ExperimentalUnsignedTypes::class)
class SmartScaling private constructor(vararg args: Int) :
	ModelTransform(modelSmartScaling(args)) {

	companion object {
		fun none(w: Int, h: Int) = SmartScaling(w, h, 0)

		fun x(w: Int, h: Int, ww: Int, hh: Int) = SmartScaling(w, h, 1, ww, hh)

		fun y(w: Int, h: Int, ww: Int, hh: Int) = SmartScaling(w, h, 2, ww, hh)

		fun both(w: Int, h: Int, ww: Int, hh: Int) = SmartScaling(w, h, 3, ww, hh)
	}
}

@OptIn(ExperimentalUnsignedTypes::class)
class FullScaling(w: Int, h: Int) : ModelTransform(modelFullScaling(intArrayOf(w, h)))
