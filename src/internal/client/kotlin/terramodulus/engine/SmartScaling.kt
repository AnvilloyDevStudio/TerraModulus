/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.modelSmartScaling

class SmartScaling(w: Int, h: Int) : ModelTransform(modelSmartScaling(arrayOf(w, h))) {
}
