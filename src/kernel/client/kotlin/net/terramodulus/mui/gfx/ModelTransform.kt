/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gfx

typealias ModelTransform = net.terramodulus.engine.ModelTransform

typealias SmartScaling = net.terramodulus.engine.SmartScaling

typealias FullScaling = net.terramodulus.engine.FullScaling

fun FullScaling(rect: Dimension2I) = FullScaling(rect.width, rect.height)
