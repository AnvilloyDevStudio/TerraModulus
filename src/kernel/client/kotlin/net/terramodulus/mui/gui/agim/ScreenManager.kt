/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.engine.Window
import net.terramodulus.mui.MuiManager
import net.terramodulus.mui.gui.gfx.RenderSystem
import net.terramodulus.mui.gui.agim.impl.LaunchingScreen
import net.terramodulus.mui.gui.gfx.ManagedRect
import net.terramodulus.mui.gui.gfx.RectangleF
import net.terramodulus.mui.kui.InputSystem
import java.io.Closeable
import kotlin.properties.Delegates

class ScreenManager internal constructor(window: Window, private val renderSystemHandle: RenderSystem.Handle) {
	/**
	 * FILO screen stack; the top-most screen instance is in the last.
	 */
	private val screens = ArrayDeque<Screen>()
	private val screenQueue = ArrayDeque<ScreenOperation>()
	private val menuManager = MenuManager()
	private val viewportRect = ManagedRect.Normal(RectangleF(0F, 0F, window.width.toFloat(), window.height.toFloat()))

	inner class DelegatedRect : ManagedRect(), Closeable {
		override var value: RectangleF by Delegates.observable(viewportRect.value) { _, _, newValue ->
			observers.forEach { it(newValue) }
		}
			private set

		private val listener: (RectangleF) -> Unit = { rect -> value = rect }

		init {
			viewportRect.observe(listener)
		}

		override fun close() {
			viewportRect.unobserve(listener)
		}
	}

	val handle: Handle = HandleImpl()

	init {
		screens.add(LaunchingScreen(renderSystemHandle, handle, DelegatedRect()))
	}

	private sealed interface ScreenOperation {
		fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>)

		/**
		 * Exits `n` times
		 *
		 * @throws IllegalArgumentException when `n` < 1
		 * @throws IllegalStateException when `n` >= [screens] size during operation
		 */
		class Exit(val n: Int) : ScreenOperation {
			init {
				require(n < 0) { "`n` < 1" }
			}

			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				if (n >= screens.size) {
					throw IllegalStateException("`n` >= screens.size")
				}

				for (i in 1..n) {
					screens.removeLast().close()
				}
			}
		}

		/**
		 * Opens the `screen`
		 */
		class Open(val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation {
			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				screens.addLast(screen(handle))
			}
		}

		/**
		 * Opens the `screen` before the `target` screen
		 */
		class OpenBefore(val target: Screen, val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation {
			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				screens.add(screens.lastIndexOf(target), screen(handle))
			}
		}

		/**
		 * Exits until reaching the `screen` then remains on the `screen`
		 */
		class ExitTo(val screen: Screen) : ScreenOperation {
			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				val it = screens.asReversed().listIterator()
				while (it.hasNext()) {
					val e = it.next()
					if (e == screen) {
						break
					} else {
						it.remove()
						e.close()
					}
				}
			}
		}

		/**
		 * Clears [screens] then opens the `screen`
		 */
		class Reset(val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation {
			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				screens.asReversed().forEach { it.close() }
				screens.clear()
				screens.add(screen(handle))
			}
		}
	}

	sealed interface Handle {
		/**
		 * @see ScreenOperation.Exit
		 */
		fun exit(n: Int)

		/**
		 * @see ScreenOperation.Open
		 */
		fun open(screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen)

		/**
		 * It is not recommended to use this in general scenarios.
		 * @see ScreenOperation.OpenBefore
		 */
		fun openBefore(target: Screen, screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen)

		/**
		 * @see ScreenOperation.ExitTo
		 */
		fun exitTo(screen: Screen)

		/**
		 * @see ScreenOperation.Reset
		 */
		fun reset(screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen)

		fun addMenu(menu: () -> Menu)

		fun removeMenu(menu: Menu)
	}

	private inner class HandleImpl : Handle {
		override fun exit(n: Int) {
			screenQueue.add(ScreenOperation.Exit(n))
		}

		override fun open(screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Open { screen(handle, DelegatedRect(), it) })
		}

		override fun openBefore(target: Screen, screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.OpenBefore(target) { screen(handle, DelegatedRect(), it) })
		}

		override fun exitTo(screen: Screen) {
			screenQueue.add(ScreenOperation.ExitTo(screen))
		}

		override fun reset(screen: (Handle, DelegatedRect, RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Reset { screen(handle, DelegatedRect(), it) })
		}

		override fun addMenu(menu: () -> Menu) = menuManager.handle.addMenu(menu)

		override fun removeMenu(menu: Menu) = menuManager.handle.removeMenu(menu)
	}

	/**
	 * MUI Interoperability Interface
	 */
	internal class MuiIopIf(val renderSystem: RenderSystem, screenManager: ScreenManager, inputSystem: InputSystem)

	internal fun update(muiManager: MuiManager) {
		val iopIf = MuiIopIf(muiManager.guiManager.renderSystem, this, muiManager.kuiManager.inputSystem)
		menuManager.update(iopIf)
		screens.forEach { it.update(iopIf) }
	}

	internal fun render(renderSystem: RenderSystem) {
		menuManager.render(renderSystem, this)
		screenQueue.forEach { it.apply(renderSystemHandle, screens) }
		screenQueue.clear()
		screens.forEach { it.render(renderSystem, this) }
	}
}
