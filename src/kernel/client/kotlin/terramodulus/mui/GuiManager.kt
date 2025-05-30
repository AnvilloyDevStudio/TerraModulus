/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui

import terramodulus.engine.Window
import java.io.Closeable

class GuiManager : Closeable {
	private val window = Window()
	private val canvas = window.canvas
	val renderSystem = RenderSystem()
	val inputSystem = InputSystem()

	override fun close() {
		window.close()
	}
}
