/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.asd

import net.terramodulus.mui.gui.gfx.RectangleF

/**
 * Since rectangles are modified only during layout processing,
 * further follow-ups must not be deferred to next tick.
 */
abstract class AsdHandle internal constructor() {
	/**
	 * Caveat: Must only be modified by [Layout][net.terramodulus.mui.gui.agim.Layout].
	 * When modified, [triggerObservers] must be invoked.
	 */
	abstract var rect: RectangleF
		internal set

	protected val observers = LinkedHashSet<() -> Unit>()

	internal fun observe(observer: () -> Unit) {
		observers.add(observer)
	}

	internal fun unobserve(observer: () -> Unit) {
		observers.remove(observer)
	}

	internal fun triggerObservers() {
		observers.forEach { it() }
	}

	/**
	 * Registers ASD Processors from AGIMOs to [AsdManager].
	 */
	abstract fun registerAsdProcessor(processor: AsdProcessor<*>)

	// TODO likely those below are useless
	abstract class Container : AsdHandle() {}

	abstract class Menu : Container() {}

	abstract class Screen : Container() {}

// 	interface Component : LayoutHandle {} // Do we need this?
}
