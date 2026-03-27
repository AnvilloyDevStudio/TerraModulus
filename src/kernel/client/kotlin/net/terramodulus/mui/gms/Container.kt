/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms

import net.terramodulus.mui.gfx.ManagedRect

sealed interface Container {
	val rect: ManagedRect
}
