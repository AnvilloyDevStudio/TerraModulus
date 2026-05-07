/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.agim.event.MenuEvent
import net.terramodulus.mui.gui.gfx.RenderSystem
import java.io.Closeable

abstract class Menu : Container, Closeable {
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

	internal fun render(renderSystem: RenderSystem) {
		layout.components.forEach { it.render(renderSystem) }
	}

	internal fun update(muiIopIf: ScreenManager.MuiIopIf) {
		dispatchEvent(MenuEvent.Update(muiIopIf))
		layout.update()
		layout.components.forEach { it.update(muiIopIf) }
	}

	/**
	 * Cleans up and closes any used resources in this session.
	 */
	final override fun close() {
		dispatchEvent(MenuEvent.Close)
	}
}
