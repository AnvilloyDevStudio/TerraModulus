/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gms

import net.terramodulus.mui.gfx.RenderSystem
import net.terramodulus.mui.gms.impl.LaunchingScreen
import net.terramodulus.mui.input.InputSystem

class ScreenManager internal constructor(private val renderSystemHandle: RenderSystem.Handle) {
	/**
	 * FILO screen stack; the top-most screen instance is in the last.
	 */
	private val screens = ArrayDeque<Screen>()
	private val screenQueue = ArrayDeque<ScreenOperation>()
	val handle: Handle = HandleImpl()

	init {
		screens.add(LaunchingScreen(renderSystemHandle))
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
					screens.removeLast().exit()
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
		class OpenBefore(val screen: (RenderSystem.Handle) -> Screen, val target: Screen) : ScreenOperation {
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
						e.exit()
					}
				}
			}
		}

		/**
		 * Clears [screens] then opens the `screen`
		 */
		class Reset(val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation {
			override fun apply(handle: RenderSystem.Handle, screens: ArrayDeque<Screen>) {
				screens.asReversed().forEach { it.exit() }
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
		fun open(screen: (RenderSystem.Handle) -> Screen)

		/**
		 * It is not recommended to use this in general scenarios.
		 * @see ScreenOperation.OpenBefore
		 */
		fun openBefore(screen: (RenderSystem.Handle) -> Screen, target: Screen)

		/**
		 * @see ScreenOperation.ExitTo
		 */
		fun exitTo(screen: Screen)

		/**
		 * @see ScreenOperation.Reset
		 */
		fun reset(screen: (RenderSystem.Handle) -> Screen)
	}

	private inner class HandleImpl : Handle {
		override fun exit(n: Int) {
			screenQueue.add(ScreenOperation.Exit(n))
		}

		override fun open(screen: (RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Open(screen))
		}

		override fun openBefore(screen: (RenderSystem.Handle) -> Screen, target: Screen) {
			screenQueue.add(ScreenOperation.OpenBefore(screen, target))
		}

		override fun exitTo(screen: Screen) {
			screenQueue.add(ScreenOperation.ExitTo(screen))
		}

		override fun reset(screen: (RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Reset(screen))
		}
	}

	internal fun update(renderSystem: RenderSystem, inputSystem: InputSystem) {
		screens.forEach { it.update(renderSystem, this, inputSystem) }
	}

	internal fun render(renderSystem: RenderSystem) {
		screenQueue.forEach { it.apply(renderSystemHandle, screens) }
		screenQueue.clear()
		screens.forEach { it.render(renderSystem, this) }
	}
}
