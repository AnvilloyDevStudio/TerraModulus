/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.event

import com.cout970.math.vec2.Vec2d
import net.terramodulus.mui.gui.agim.ScreenManager
import net.terramodulus.mui.kui.InputSystem

sealed interface ScreenEvent {
	data class Update(val muiIoI: ScreenManager.MuiIoI) : ScreenEvent
	data class MouseDown(override val pos: Vec2d, override val keyId: InputSystem.KeyId) :
		ScreenEvent, GenericEvents.MouseDown(), BubblingEvent by BubblingEventImpl()
	data class MouseUp(override val pos: Vec2d, override val keyId: InputSystem.KeyId) :
		ScreenEvent, GenericEvents.MouseUp(), BubblingEvent by BubblingEventImpl()
	data object Close : ScreenEvent
	data class Generic<T : GenericEvent>(val generic: T) : ScreenEvent
}
