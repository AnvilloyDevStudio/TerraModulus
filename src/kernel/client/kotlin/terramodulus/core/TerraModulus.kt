/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import terramodulus.common.core.AbstractTerraModulus
import terramodulus.mui.GuiManager

class TerraModulus internal constructor() : AbstractTerraModulus() {
	private val guiManager = GuiManager()

	override var tps: Int
		get() = TODO("Not yet implemented")
		set(value) {}

	override fun run() {
		guiManager.showWindow()
		while (true) {
			guiManager.updateCanvas()
			Thread.sleep(1)
		}
	}

	override fun close() {
		TODO("Not yet implemented")
	}
}
