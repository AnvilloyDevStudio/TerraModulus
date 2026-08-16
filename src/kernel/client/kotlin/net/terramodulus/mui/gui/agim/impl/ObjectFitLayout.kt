/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import com.cout970.math.vec2.ImmVec2d
import net.terramodulus.mui.gui.agim.AnchorAlignmentHelper
import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.asd.AsdHandle
import net.terramodulus.mui.gui.gfx.Dimension2D
import net.terramodulus.mui.gui.gfx.RectangleF
import kotlin.math.max
import kotlin.math.min

/**
 * Only applicable to AGIMO elements with intrinsic ratios.
 */
class ObjectFitLayout(
	container: Container,
	component: Component,
	private var mode: ModeConfig,
	private var alignment: AlignmentConfig,
) : Layout(container) {
	override val components = componentsSequence(::component)
	var component = component
		private set

	// TODO should Fill be implemented?
	enum class ModeConfig {
		Contain {
			override fun compute(container: RectangleF, component: IntrinsicRatioProperty): Dimension2D {
				val w = container.width.toDouble() / component.width.toDouble()
				val h = container.height.toDouble() / component.height.toDouble()
				val scale = min(w, h)
				return Dimension2D(
					component.width.toDouble() * scale,
					component.height.toDouble() * scale,
				)
			}
		},
		Cover {
			override fun compute(container: RectangleF, component: IntrinsicRatioProperty): Dimension2D {
				val w = container.width.toDouble() / component.width.toDouble()
				val h = container.height.toDouble() / component.height.toDouble()
				val scale = max(w, h)
				return Dimension2D(
					component.width.toDouble() * scale,
					component.height.toDouble() * scale,
				)
			}
		},
		;

		abstract fun compute(container: RectangleF, component: IntrinsicRatioProperty): Dimension2D
	}

	/**
	 * Range within `[0,1]`
	 */
	data class AlignmentConfig(val x: Double, val y: Double) {
		companion object {
			val DEFAULT = AlignmentConfig(0.5, 0.5)
			fun withX(x: Double) = AlignmentConfig(x, 0.5)
			fun withY(y: Double) = AlignmentConfig(y, 0.5)
		}
	}

	fun update(component: Component) {
		operate {
			this@ObjectFitLayout.component = component
		}
	}

	interface Config {
		var mode: ModeConfig
		var alignment: AlignmentConfig
	}

	fun update(operation: Config.() -> Unit) {
		operate {
			operation(object : Config {
				override var mode: ModeConfig by this@ObjectFitLayout::mode
				override var alignment: AlignmentConfig by this@ObjectFitLayout::alignment
			})
		}
	}

	private operator fun RectangleF.times(other: AlignmentConfig) = ImmVec2d(width * other.x, height * other.y)
	private operator fun Dimension2D.times(other: AlignmentConfig) = ImmVec2d(width * other.x, height * other.y)

	override fun layOut(handle: AsdHandle) {
		val ratio = component.asdHandle.getProperty(IntrinsicRatioProperty.KEY)
		if (ratio === null)
			throw IllegalStateException("component has no intrinsic ratio")
		val target = mode.compute(handle.rect, ratio)
		component.asdHandle.rect = AnchorAlignmentHelper.Subject(handle.rect.toDouble(), handle.rect * alignment)
			.alignTarget(AnchorAlignmentHelper.Target(target, target * alignment)).toFloat()
		component.asdHandle.triggerRectObservers()
	}
}
