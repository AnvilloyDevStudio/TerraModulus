/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.addColorFilter
import terramodulus.engine.ferricia.Mui.addModelTransform

sealed class Drawable(internal val handle: ULong) {
	fun add(model: ModelTransform) = addModelTransform(handle, model.wideHandle)

	fun add(filter: ColorFilter) = addColorFilter(handle, filter.wideHandle)
}
