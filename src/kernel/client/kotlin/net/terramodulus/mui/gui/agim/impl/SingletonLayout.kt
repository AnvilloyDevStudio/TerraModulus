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
import net.terramodulus.mui.gui.gfx.InsetsF
import net.terramodulus.mui.gui.gfx.RectangleF
import kotlin.math.max
import kotlin.math.min

class SingletonLayout(container: Container, component: Component, private var config: Config) : Layout(container) {
	override val components = componentsSequence(::component)
	var component = component
		private set

	sealed class Config private constructor() {
		abstract fun layOut(container: RectangleF): RectangleF

		sealed class Absolute private constructor() : Config() {
			data object Full : Config() {
				override fun layOut(container: RectangleF) = container
			}

			data class Insets(var insets: InsetsF) : Config() {
				override fun layOut(container: RectangleF) = container - insets
			}
		}

		enum class ObjectFit {
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

			private operator fun RectangleF.times(other: ObjectFitLayout.AlignmentConfig) = ImmVec2d(width * other.x, height * other.y)
			private operator fun Dimension2D.times(other: ObjectFitLayout.AlignmentConfig) = ImmVec2d(width * other.x, height * other.y)

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

	}

	fun update(component: Component) {
		operate {
			this@SingletonLayout.component = component
		}
	}

	fun update(operation: (Config) -> Config) {
		operate {
			config = operation(config)
		}
	}

	override fun layOut(handle: AsdHandle) {
		component.asdHandle.rect = config.layOut(handle.rect)
		component.asdHandle.triggerRectObservers()
	}
}
