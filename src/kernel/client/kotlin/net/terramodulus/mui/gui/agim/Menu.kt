/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.agim.event.MenuEvent

abstract class Menu : Container {
	private val listeners = HashMap<Class<out MenuEvent>, LinkedHashSet<(MenuEvent) -> Unit>>()

	fun <T: MenuEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (MenuEvent) -> Unit)
	}

	fun <T: MenuEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	internal fun dispatchEvent(event: MenuEvent) {
		listeners[event.javaClass]?.forEach { it(event) }
	}

	abstract fun render()

	internal fun update(muiIopIf: ScreenManager.MuiIopIf) {
		layout.update()
		layout.components.forEach { it.update(muiIopIf) }
	}
}
