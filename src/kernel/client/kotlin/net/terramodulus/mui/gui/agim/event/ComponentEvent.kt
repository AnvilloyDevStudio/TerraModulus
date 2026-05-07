/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.event

import net.terramodulus.mui.gui.agim.ScreenManager

sealed interface ComponentEvent {
	data class Update(val muiIopIf: ScreenManager.MuiIopIf) : ComponentEvent
	data class Key(val generic: GenericEvent.Key) : ComponentEvent
}
