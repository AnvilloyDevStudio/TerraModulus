/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.editAlphaFilter
import terramodulus.engine.ferricia.Mui.filterAlphaFilter

@OptIn(ExperimentalUnsignedTypes::class)
class AlphaFilter(alpha: Float) : ColorFilter(filterAlphaFilter(alpha)) {
	var alpha = alpha
		set(value) {
			editAlphaFilter(handle, value)
			field = value
		}
}
