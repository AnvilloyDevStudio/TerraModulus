/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Mui.addColorFilter
import net.terramodulus.engine.ferricia.Mui.addModelTransform
import net.terramodulus.engine.ferricia.Mui.setGeomPos

sealed class Drawable(internal val handle: ULong) {
	fun add(model: ModelTransform) = addModelTransform(handle, model.wideHandle)

	fun add(filter: ColorFilter) = addColorFilter(handle, filter.wideHandle)

	fun setPos(pos: FloatArray) = setGeomPos(handle, pos)
}
