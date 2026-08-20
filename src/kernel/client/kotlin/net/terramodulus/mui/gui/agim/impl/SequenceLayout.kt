/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.agim.LayoutComputationGroup
import net.terramodulus.mui.gui.agim.LayoutComputationUnit
import net.terramodulus.mui.gui.agim.LayoutHandle
import net.terramodulus.mui.gui.agim.getProperty
import net.terramodulus.mui.gui.asd.AsdHandle
import net.terramodulus.mui.gui.gfx.Anchor5
import net.terramodulus.mui.gui.gfx.Direction2

/**
 * Common implementation that is either [ColumnLayout] or [RowLayout].
 *
 * This is an optimized special version of [FlexibleBoxLayout] without any expected
 * multiple *sequences* of components in a single layout.
 */
sealed class SequenceLayout(
	container: Container,
	override val elements: ElementList<Element>,
	protected var config: Config,
) : Layout.ElementGroup<SequenceLayout.Element>(container, elements) {
	class Element {
		companion object {
			fun default() = Element()
		}
	}

	/**
	 * [padding] is the paddings from the four edges.
	 * [gap] is the gaps only in between elements.
	 */
	class Config(val direction: Direction2, val gap: Double = 0.0, val padding: Double = 0.0)

	interface ConfigEnv {
		var config: Config
	}

	fun update(operation: ConfigEnv.() -> Unit) {
		operate { operation(object : ConfigEnv {
			override var config: Config by this@SequenceLayout::config
		}) }
	}

	override fun add(component: Component) = elements.add(component, Element.default())

	override fun addBefore(target: Component, component: Component) =
		elements.addBefore(target, component, Element.default())

	override fun addAfter(target: Component, component: Component) =
		elements.addAfter(target, component, Element.default())

	override fun replace(target: Component, component: Component) =
		elements.replace(target, component, Element.default())
}

/**
 * **Column** case of [SequenceLayout].
 */
class ColumnLayout private constructor(container: Container, elements: ElementList<Element>, config: Config) :
	SequenceLayout(container, elements, config) {
	companion object {
		fun withComponents(vararg components: Component, config: Config) = { it: Container ->
			ColumnLayout(it, ElementList.withComponentsDefault(Element::default, *components), config)
		}

		fun withComponents(components: Collection<Component>, config: Config) = { it: Container ->
			ColumnLayout(it, ElementList.withComponentsDefault(Element::default, components), config)
		}

		fun withElements(vararg elements: Pair<Component, Element>, config: Config) = { it: Container ->
			ColumnLayout(it, ElementList.withElements(*elements), config)
		}

		fun withElements(elements: Map<Component, Element>, config: Config) = { it: Container ->
			ColumnLayout(it, ElementList.withElements(elements), config)
		}
	}

	override fun layOut(handle: LayoutHandle) = sequenceOf(LayoutComputationGroup({
		put(container.asdHandle, setOf(RectangleProperty.KEY))
	}, {
		val containerRect = getUnit(container.asdHandle).properties.getProperty(RectangleProperty.KEY)!!.value
		var anchor = containerRect.anchor(when (config.direction) {
			Direction2.Positive -> Anchor5.BottomLeft
			Direction2.Negative -> Anchor5.TopLeft
		})
		elements.map {
			LayoutComputationUnit({
				put(container.asdHandle, setOf(IntrinsicDimensionsProperty.KEY))
			}, {

			}, {

			})
		}.toSet()
	}))
}

/**
 * **Row** case of [SequenceLayout].
 */
class RowLayout private constructor(container: Container, elements: ElementList<Element>, config: Config) :
	SequenceLayout(container, elements, config) {
	companion object {
		fun withComponents(vararg components: Component) = { it: Container ->
			RowLayout(it, ElementList.withComponentsDefault(Element::default, *components))
		}

		fun withComponents(components: Collection<Component>) = { it: Container ->
			RowLayout(it, ElementList.withComponentsDefault(Element::default, components))
		}

		fun withElements(vararg elements: Pair<Component, Element>) = { it: Container ->
			RowLayout(it, ElementList.withElements(*elements))
		}

		fun withElements(elements: Map<Component, Element>) = { it: Container ->
			RowLayout(it, ElementList.withElements(elements))
		}
	}

	override fun layOut(handle: AsdHandle) {
		elements.forEach { TODO() }
	}
}
