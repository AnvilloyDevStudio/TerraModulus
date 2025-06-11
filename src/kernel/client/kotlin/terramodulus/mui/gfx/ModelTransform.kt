/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

typealias ModelTransform = terramodulus.engine.ModelTransform

typealias SmartScaling = terramodulus.engine.SmartScaling

typealias FullScaling = terramodulus.engine.FullScaling

fun FullScaling(rect: Dimension2I) = FullScaling(rect.width, rect.height)
