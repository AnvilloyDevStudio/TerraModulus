/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.impl.LaunchingScreen

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
		 * @see ScreenOperation.ExitTo
		 */
		fun exitTo(screen: Screen)

		/**
		 * @see ScreenOperation.Reset
		 */
		fun reset(screen: (RenderSystem.Handle) -> Screen)
	}

	private inner class HandleImpl : Handle {
		/**
		 * @see ScreenOperation.Exit
		 */
		override fun exit(n: Int) {
			screenQueue.add(ScreenOperation.Exit(n))
		}

		/**
		 * @see ScreenOperation.Open
		 */
		override fun open(screen: (RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Open(screen))
		}

		/**
		 * @see ScreenOperation.ExitTo
		 */
		override fun exitTo(screen: Screen) {
			screenQueue.add(ScreenOperation.ExitTo(screen))
		}

		/**
		 * @see ScreenOperation.Reset
		 */
		override fun reset(screen: (RenderSystem.Handle) -> Screen) {
			screenQueue.add(ScreenOperation.Reset(screen))
		}
	}

// 	internal fun update() {
// 		screens.forEach { it.update() }
// 	}

	internal fun render(renderSystem: RenderSystem) {
		screenQueue.forEach { it.apply(renderSystemHandle, screens) }
		screenQueue.clear()
		screens.forEach { it.render(renderSystem, this) }
	}
}
