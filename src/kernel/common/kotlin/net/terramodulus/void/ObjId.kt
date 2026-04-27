/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.void

import kotlin.random.Random
import kotlin.random.nextUInt

@JvmInline
value class ObjId(private val value: UInt) {
	companion object {
		fun random() = ObjId(Random.nextUInt())

		fun randomUnique(map: Map<ObjId, *>): ObjId {
			var id: ObjId
			do id = random()
			while (map.containsKey(id))
			return id
		}
	}
}
