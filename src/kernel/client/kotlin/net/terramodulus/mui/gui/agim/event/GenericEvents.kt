/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.event

import com.cout970.math.vec2.Vec2d
import net.terramodulus.mui.kui.InputSystem

object GenericEvents {
	abstract class MouseEnter internal constructor()
	abstract class MouseLeave internal constructor()
	abstract class MouseMove internal constructor() {
		abstract val a: Vec2d
		abstract val b: Vec2d
	}
	abstract class MouseDown internal constructor() {
		abstract val pos: Vec2d
		abstract val keyId: InputSystem.KeyId
	}
	abstract class MouseUp internal constructor() {
		abstract val pos: Vec2d
		abstract val keyId: InputSystem.KeyId
	}
}
