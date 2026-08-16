/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.asd.AsdHandle
import net.terramodulus.mui.gui.gfx.Dimension2D

/**
 * Scaling intrinsic dimensions of AGIMO element for intrinsic dimensions for this object.
 */
class ScaledLayout(
	container: Container,
	component: Component,
	private var config: Config,
) : Layout(container) {
	override val components = componentsSequence(::component)
	var component = component
		private set

	sealed class Config {
		abstract fun compute(dim: IntrinsicDimensionsProperty): Dimension2D

		/**
		 * Scale both dimensions by the same scaling
		 * @param scale `> 0`
		 */
		data class Scale(val scale: Double) : Config() {
			override fun compute(dim: IntrinsicDimensionsProperty) =
				Dimension2D(dim.width.toDouble() * scale, dim.height.toDouble() * scale)
		}

		class Compute private constructor(private val x: Value, private val y: Value) : Config() {
			private object MathEnvImpl : MathEnv

			constructor(x: MathEnv.() -> Value, y: MathEnv.() -> Value) : this(x(MathEnvImpl), y(MathEnvImpl))

			sealed interface Value {
				fun compute(dim: IntrinsicDimensionsProperty): Double

				operator fun plus(that: Value) = Operator.Plus(this, that)
				operator fun minus(that: Value) = Operator.Minus(this, that)
				operator fun times(that: Value) = Operator.Times(this, that)
				operator fun div(that: Value) = Operator.Div(this, that)
			}

			sealed class Operator private constructor() : Value {
				data class Plus(val a: Value, val b: Value) : Operator() {
					override fun compute(dim: IntrinsicDimensionsProperty) = a.compute(dim) + b.compute(dim)
				}
				data class Minus(val a: Value, val b: Value) : Operator() {
					override fun compute(dim: IntrinsicDimensionsProperty) = a.compute(dim) - b.compute(dim)
				}
				data class Times(val a: Value, val b: Value) : Operator() {
					override fun compute(dim: IntrinsicDimensionsProperty) = a.compute(dim) * b.compute(dim)
				}
				data class Div(val a: Value, val b: Value) : Operator() {
					override fun compute(dim: IntrinsicDimensionsProperty) = a.compute(dim) / b.compute(dim)
				}
			}

			sealed class Param private constructor() : Value {
				data class Num(val value: Double) : Param() {
					override fun compute(dim: IntrinsicDimensionsProperty) = value
				}
				data object DimX : Param() {
					override fun compute(dim: IntrinsicDimensionsProperty) = dim.width.toDouble()
				}
				data object DimY : Param() {
					override fun compute(dim: IntrinsicDimensionsProperty) = dim.height.toDouble()
				}
			}

			sealed interface MathEnv {
				val dimX: Param get() = Param.DimX
				val dimY: Param get() = Param.DimY
				fun num(value: Double) = Param.Num(value)
			}

			override fun compute(dim: IntrinsicDimensionsProperty) = Dimension2D(x.compute(dim), y.compute(dim))
		}
	}

	fun update(component: Component) {
		operate {
			this@ScaledLayout.component = component
		}
	}

	fun update(operation: (Config) -> Config) {
		operate {
			config = operation(config)
		}
	}

	override fun layOut(handle: AsdHandle) {
		val dim = component.asdHandle.getProperty(IntrinsicDimensionsProperty.KEY)
		if (dim === null)
			throw IllegalStateException("component has no intrinsic dimensions")
		config.compute(dim)
		TODO("Then?")
	}
}
