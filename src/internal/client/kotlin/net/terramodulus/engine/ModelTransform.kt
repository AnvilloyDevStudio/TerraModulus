/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import com.cout970.math.vec2.MutVec2d
import com.cout970.math.vec2.Vec2d
import net.terramodulus.engine.ferricia.Mui.modelFullScaling
import net.terramodulus.engine.ferricia.Mui.modelGeneralTransform
import net.terramodulus.engine.ferricia.Mui.modelSmartScaling
import net.terramodulus.engine.ferricia.Mui.updateGeneralTransform
import kotlin.properties.Delegates

@OptIn(ExperimentalUnsignedTypes::class)
sealed class ModelTransform(handles: ULongArray) {
	internal val handle: ULong = handles[0]
	internal val wideHandle: ULong = handles[1]
}

@OptIn(ExperimentalUnsignedTypes::class)
class GeneralTransform(sx: Double, sy: Double, angle: Double, px: Double, py: Double) :
	ModelTransform(modelGeneralTransform(doubleArrayOf(sx, sy, angle, px, py))) {
	var scale: Vec2d by Delegates.observable(MutVec2d(sx, sy)) { _, _, new ->
		updateGeneralTransform(handle, doubleArrayOf(new.x, new.y, angle, px, py))
	}
	var angle: Double by Delegates.observable(angle) { _, _, new ->
		updateGeneralTransform(handle, doubleArrayOf(sx, sy, new, px, py))
	}
	var pos: Vec2d by Delegates.observable(MutVec2d(px, py)) { _, _, new ->
		updateGeneralTransform(handle, doubleArrayOf(sx, sy, angle, new.x, new.y))
	}
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
