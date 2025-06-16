/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms.impl

import terramodulus.mui.gfx.RectangleF
import terramodulus.mui.gms.Component
import terramodulus.mui.gms.Container
import terramodulus.mui.gms.Layout

/**
 * Common implementation that is either [ColumnLayout] or [RowLayout].
 *
 * This is an optimized special version of [FlexibleBoxLayout] without any expected
 * multiple *sequences* of components in a single layout.
 */
sealed class SequenceLayout(container: Container, protected val elements: ElementList<Element>) : Layout(container) {
	final override val components = elements.componentsView

	class Element {
		companion object {
			fun default() = Element()
		}
	}
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
