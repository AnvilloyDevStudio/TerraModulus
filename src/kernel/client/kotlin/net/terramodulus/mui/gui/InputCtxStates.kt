/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui

import net.terramodulus.mui.gui.asd.AsdHandle

sealed class InputCtxStates<S : InputState, K : Any>(protected val asdHandle: AsdHandle) {
	protected val listeners = mutableSetOf<InputState.Listener<S, K>>()
}
