/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.impl.LaunchingScreen

class ScreenManager internal constructor(private val renderSystemHandle: RenderSystem.Handle) {
	private val screens = ArrayDeque<Screen>()
	private val screenQueue = ArrayDeque<ScreenOperation>()
	private val handle: Handle = HandleImpl()

	init {
		screens.add(LaunchingScreen(renderSystemHandle))
	}

	private sealed interface ScreenOperation {
		/**
		 * Exits `n` times
		 *
		 * @throws IllegalArgumentException when `n` < 1
		 * @throws IllegalStateException when `n` >= [screens] size during operation
		 */
		class Exit(val n: Int) : ScreenOperation

		/**
		 * Opens the `screen`
		 */
		class Open(val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation

		/**
		 * Exits until reaching the `screen` then remains on the `screen`
		 */
		class ExitTo(val screen: Screen) : ScreenOperation

		/**
		 * Clears [screens] then opens the `screen`
		 */
		class Reset(val screen: (RenderSystem.Handle) -> Screen) : ScreenOperation
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

	internal fun render(renderSystem: RenderSystem) {
		screens.forEach { it.render(renderSystem) }
	}
}
