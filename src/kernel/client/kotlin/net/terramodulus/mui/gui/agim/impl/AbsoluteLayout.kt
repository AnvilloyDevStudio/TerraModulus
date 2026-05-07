/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.gfx.InsetsD
import net.terramodulus.mui.gui.gfx.InsetsF
import net.terramodulus.mui.gui.gfx.RectangleF

class AbsoluteLayout(container: Container, component: Component, private var config: Config) : Layout(container) {
	override val components = componentsSequence(::component)
	var component = component
		private set

	sealed class Config private constructor() {
		abstract fun layout(container: RectangleF): RectangleF

		data object Full : Config() {
			override fun layout(container: RectangleF) = container
		}

		data class Insets(var insets: InsetsF) : Config() {
			override fun layout(container: RectangleF) = container - insets
		}
	}

	fun update(component: Component) {
		operate {
			this@AbsoluteLayout.component = component
		}
	}

	fun update(operation: (Config) -> Config) {
		operate {
			config = operation(config)
		}
	}

	override fun layout(rect: RectangleF) {
		component.rect.value = config.layout(rect)
	}
}
