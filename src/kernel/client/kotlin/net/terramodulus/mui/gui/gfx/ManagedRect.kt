/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.gfx

import kotlin.properties.Delegates.observable

/**
 * AGIM internally Managed Rectangle
 */
abstract class ManagedRect {
	abstract val value: RectangleF

	protected val observers = LinkedHashSet<(RectangleF) -> Unit>()

	// What is this?
	internal abstract fun setValue(rect: RectangleF)

	class Normal(rect: RectangleF) : ManagedRect() {
		override var value: RectangleF by observable(rect) { _, _, newValue -> observers.forEach { it(newValue) } }
			internal set

		override fun setValue(rect: RectangleF) {
			value = rect
		}
	}

	internal fun observe(observer: (RectangleF) -> Unit) {
		observers.add(observer)
	}

	internal fun unobserve(observer: (RectangleF) -> Unit) {
		observers.remove(observer)
	}
}
