/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.newSimpleLineGeom

class SimpleLineGeom(x0: Int, y0: Int, x1: Int, y1: Int, r: Int, g: Int, b: Int, a: Int) :
	GeoDrawable(newSimpleLineGeom(arrayOf(x0, y0, x1, y1, r, g, b, a))) {

}
