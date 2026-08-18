/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.AbstractPane
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.asd.AsdHandle
import net.terramodulus.mui.gui.gfx.RenderSystem

class SimplePane(asdHandle: AsdHandle, override var layout: Layout) : AbstractPane(asdHandle) {
	override fun render(renderSystem: RenderSystem) {
		layout.render(renderSystem)
	}
}
