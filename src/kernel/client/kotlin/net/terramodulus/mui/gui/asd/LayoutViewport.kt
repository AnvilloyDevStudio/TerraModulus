/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.asd

import java.util.LinkedList

// TODO Do we really need this?
//   Maybe somehow this could become/transform into a visitor helper?
class LayoutViewport {
	private val treeRoot = RootNode()

	private open class Node {
		/**
		 * Should be read-only outside this class scope;
		 * modifications and state management should be managed within this class scope
		 */
		val children = LinkedList<Child>()

		open class Child(val parent: Node) : Node()
	}

	private class RootNode : Node() {
		val screens = ScreenTree(this)
		val menus = MenuTree(this)
	}

	private open class TopContainerNode(parent: Node) : Node.Child(parent) {

	}

	private class ScreenTree(parent: Node) : Node.Child(parent) {
		val menus = MenuTree(this)
	}

	private class MenuTree(parent: Node) : Node.Child(parent) {}
}
