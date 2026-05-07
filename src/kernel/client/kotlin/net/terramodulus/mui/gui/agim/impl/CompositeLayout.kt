/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.gfx.RectangleF

class CompositeLayout(container: Container) : Layout(container) {
	private val layouts = ArrayDeque<Layout>()

	override val components = layouts.asSequence().flatMap { it.components }

	fun update(operation: ArrayDeque<Layout>.() -> Unit) {
		operate { operation(layouts) }
	}

	override fun layout(rect: RectangleF) {
		layouts.forEach { it.update() }
	}
}
