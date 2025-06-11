/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.newSimpleLineGeom
import terramodulus.engine.ferricia.Mui.newSimpleRectGeom

sealed class GeomDrawable(handle: ULong) : Drawable(handle) {
}

class SimpleLineGeom(x0: Int, y0: Int, x1: Int, y1: Int, r: Int, g: Int, b: Int, a: Int) :
	GeomDrawable(newSimpleLineGeom(intArrayOf(x0, y0, x1, y1, r, g, b, a)))

class SimpleRectGeom(x0: Int, y0: Int, x1: Int, y1: Int, r: Int, g: Int, b: Int, a: Int) :
	GeomDrawable(newSimpleRectGeom(intArrayOf(x0, y0, x1, y1, r, g, b, a)))
