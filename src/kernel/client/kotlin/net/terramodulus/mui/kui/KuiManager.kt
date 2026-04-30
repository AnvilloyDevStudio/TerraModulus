/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.kui

import net.terramodulus.mui.kui.InputSystem.KeyEvent

class KuiManager {
	val inputSystem = InputSystem()

	internal fun update(events: List<KeyEvent>) {
		inputSystem.update(events)
	}
}
