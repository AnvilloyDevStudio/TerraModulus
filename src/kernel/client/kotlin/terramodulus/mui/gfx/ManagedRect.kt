/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import kotlin.properties.Delegates.observable

class ManagedRect(rect: RectangleF) {
	var rect: RectangleF by observable(rect) { _, _, newValue -> observers.forEach { it(newValue) } }

	private val observers = LinkedHashSet<(RectangleF) -> Unit>()

	fun observe(observer: (RectangleF) -> Unit) {
		observers.add(observer)
	}

	fun unobserve(observer: (RectangleF) -> Unit) {
		observers.remove(observer)
	}
}
