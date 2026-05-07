/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.agim.event.ScreenEvent
import net.terramodulus.mui.gui.gfx.RenderSystem
import java.io.Closeable

abstract class Screen(
	managerHandle: ScreenManager.Handle,
	final override val rect: ScreenManager.DelegatedRect
) : Container, Closeable {
	private val listeners = HashMap<Class<out ScreenEvent>, LinkedHashSet<(ScreenEvent) -> Unit>>()
	private val menuManager = MenuManager()
	val handle: Handle = HandleImpl(managerHandle)

	fun <T: ScreenEvent> addListener(e: Class<T>, l: (T) -> Unit) {
		@Suppress("UNCHECKED_CAST")
		listeners.computeIfAbsent(e) { LinkedHashSet() }.add(l as (ScreenEvent) -> Unit)
	}

	fun <T: ScreenEvent> removeListener(e: Class<T>, l: (T) -> Unit) {
		listeners[e]?.remove(l)
	}

	internal fun dispatchEvent(event: ScreenEvent) {
		listeners[event.javaClass]?.forEach { it(event) }
	}

	sealed interface Handle {
		fun addMenu(menu: () -> Menu)

		fun removeMenu(menu: Menu)

		fun addTopMenu(menu: () -> Menu)

		fun removeTopMenu(menu: Menu)
	}

	private inner class HandleImpl(private val managerHandle: ScreenManager.Handle) : Handle {
		override fun addMenu(menu: () -> Menu) = menuManager.handle.addMenu(menu)

		override fun removeMenu(menu: Menu) = menuManager.handle.removeMenu(menu)

		override fun addTopMenu(menu: () -> Menu) = managerHandle.addMenu(menu)

		override fun removeTopMenu(menu: Menu) = managerHandle.removeMenu(menu)
	}

	internal fun update(muiIopIf: ScreenManager.MuiIopIf) {
		dispatchEvent(ScreenEvent.Update(muiIopIf))
		layout.update()
		layout.components.forEach { it.update(muiIopIf) }
	}

	internal fun render(renderSystem: RenderSystem, screenManager: ScreenManager) {
		menuManager.render(renderSystem, screenManager)
		layout.components.forEach { it.render(renderSystem) }
	}

	/**
	 * Cleans up and closes any used resources in this session.
	 */
	final override fun close() {
		dispatchEvent(ScreenEvent.Close)
		rect.close()
	}
}
