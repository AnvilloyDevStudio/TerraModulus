/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.event

sealed interface ScreenEvent {
	object Open : ScreenEvent
	object Close : ScreenEvent
}
