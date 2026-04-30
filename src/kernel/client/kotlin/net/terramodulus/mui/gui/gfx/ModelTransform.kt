/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.gfx

import net.terramodulus.engine.ModelTransform
import net.terramodulus.engine.SmartScaling

typealias ModelTransform = ModelTransform

typealias SmartScaling = SmartScaling

typealias FullScaling = net.terramodulus.engine.FullScaling

fun FullScaling(rect: Dimension2I) = FullScaling(rect.width, rect.height)
