/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gms

class ScreenManager internal constructor() {
	private val screens = ArrayDeque<Screen>()
	private val queue = ArrayDeque<ScreenOperation>()
	private val handle = Handle()

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
		class Open(val screen: () -> Screen) : ScreenOperation

		/**
		 * Exits until reaching the `screen` then remains on the `screen`
		 */
		class ExitTo(val screen: Screen) : ScreenOperation

		/**
		 * Clears [screens] then opens the `screen`
		 */
		class Reset(val screen: () -> Screen) : ScreenOperation
	}

	inner class Handle {
		/**
		 * @see ScreenOperation.Exit
		 */
		fun exit(n: Int) {
			queue.add(ScreenOperation.Exit(n))
		}

		/**
		 * @see ScreenOperation.Open
		 */
		fun open(screen: () -> Screen) {
			queue.add(ScreenOperation.Open(screen))
		}

		/**
		 * @see ScreenOperation.ExitTo
		 */
		fun exitTo(screen: Screen) {
			queue.add(ScreenOperation.ExitTo(screen))
		}

		/**
		 * @see ScreenOperation.Reset
		 */
		fun reset(screen: () -> Screen) {
			queue.add(ScreenOperation.Reset(screen))
		}
	}
}
