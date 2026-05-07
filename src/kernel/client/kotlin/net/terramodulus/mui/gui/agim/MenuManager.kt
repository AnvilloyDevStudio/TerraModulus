/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.gfx.RenderSystem
import java.util.ArrayDeque

class MenuManager internal constructor() {
	private val menus = LinkedHashSet<Menu>()
	private val menuQueue = ArrayDeque<MenuOperation>()
	val handle: Handle = HandleImpl()

	private sealed interface MenuOperation {
		fun apply(menus: LinkedHashSet<Menu>)

		class Add(val menu: () -> Menu) : MenuOperation {
			override fun apply(menus: LinkedHashSet<Menu>) {
				menus.add(menu())
			}
		}

		class Remove(val menu: Menu) : MenuOperation {
			override fun apply(menus: LinkedHashSet<Menu>) {
				menus.remove(menu)
			}
		}
	}

	sealed interface Handle {
		fun addMenu(menu: () -> Menu)

		fun removeMenu(menu: Menu)
	}

	private inner class HandleImpl : Handle {
		override fun addMenu(menu: () -> Menu) {
			menuQueue.add(MenuOperation.Add(menu))
		}

		override fun removeMenu(menu: Menu) {
			menuQueue.add(MenuOperation.Remove(menu))
		}
	}

	internal fun update(muiIopIf: ScreenManager.MuiIopIf) {
		menuQueue.forEach { it.apply(menus) }
		menuQueue.clear()
	}

	internal fun render(renderSystem: RenderSystem, screenManager: ScreenManager) {
		menus.forEach { it.render(renderSystem) }
	}
}
