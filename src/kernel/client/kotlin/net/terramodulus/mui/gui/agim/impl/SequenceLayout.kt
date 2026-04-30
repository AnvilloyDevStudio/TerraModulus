/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim.impl

import net.terramodulus.mui.gui.agim.Component
import net.terramodulus.mui.gui.agim.Container
import net.terramodulus.mui.gui.agim.Layout
import net.terramodulus.mui.gui.gfx.RectangleF

/**
 * Common implementation that is either [ColumnLayout] or [RowLayout].
 *
 * This is an optimized special version of [FlexibleBoxLayout] without any expected
 * multiple *sequences* of components in a single layout.
 */
sealed class SequenceLayout(container: Container, elements: ElementList<Element>) :
	Layout.ElementGroup<SequenceLayout.Element>(container, elements) {
	class Element {
		companion object {
			fun default() = Element()
		}
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
class ColumnLayout private constructor(container: Container, elements: ElementList<Element>) :
	SequenceLayout(container, elements) {
	companion object {
		fun withComponents(vararg components: Component) = { it: Container ->
			ColumnLayout(it, ElementList.withComponentsDefault(Element::default, *components))
		}

		fun withComponents(components: Collection<Component>) = { it: Container ->
			ColumnLayout(it, ElementList.withComponentsDefault(Element::default, components))
		}

		fun withElements(vararg elements: Pair<Component, Element>) = { it: Container ->
			ColumnLayout(it, ElementList.withElements(*elements))
		}

		fun withElements(elements: Map<Component, Element>) = { it: Container ->
			ColumnLayout(it, ElementList.withElements(elements))
		}
	}

	override fun layout(rect: RectangleF) {
		elements.forEach { TODO() }
	}
}

/**
 * **Row** case of [SequenceLayout].
 */
class RowLayout private constructor(container: Container, elements: ElementList<Element>) :
	SequenceLayout(container, elements) {
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

	override fun layout(rect: RectangleF) {
		elements.forEach { TODO() }
	}
}
