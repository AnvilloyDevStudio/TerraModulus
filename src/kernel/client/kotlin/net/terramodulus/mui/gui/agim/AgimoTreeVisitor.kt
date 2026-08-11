/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import java.util.LinkedList

internal abstract class AgimoTreeVisitor {
	internal fun interface ScreenTreeVisitor {
		fun visit(): Sequence<Screen>
	}

	internal fun interface MenuTreeVisitor {
		fun visit(): Sequence<Menu>
	}

	protected class RootNode(screens: Sequence<Screen>, menus: Sequence<Menu>) {
		val screens = ScreenTree(screens)
		val menus = MenuTree(menus)
	}

	protected abstract class ContainerNode(val layout: Layout) {
		val elements = LinkedList(layout.components.map {
			if (it is AbstractPane) PaneNode(it) else SimpleComponentNode(it)
		}.toList())
	}

	protected sealed interface ComponentNode {
		val component: Component
	}

	protected class SimpleComponentNode(override val component: Component) : ComponentNode

	protected class PaneNode(override val component: AbstractPane) : ContainerNode(component.layout), ComponentNode

	protected class ScreenTree(screens: Sequence<Screen>) {
		val list = LinkedList(screens.map { ScreenNode(it) }.toList())
	}

	protected class ScreenNode(val screen: Screen) : ContainerNode(screen.layout) {
		val menus = MenuTree(screen.visit().visit())
	}

	protected class MenuTree(menus: Sequence<Menu>) {
		val list = LinkedList(menus.map { MenuNode(it) }.toList())
	}

	protected class MenuNode(val menu: Menu) : ContainerNode(menu.layout)
}
