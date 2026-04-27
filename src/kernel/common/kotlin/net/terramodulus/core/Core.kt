/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.core

const val NAME = "TerraModulus"
const val VERSION = "0.0.1" // TODO placeholder

// Caveat: Avoid passing resource path to Engine
fun getResourceAsString(path: String) =
	object {}.javaClass.getResourceAsStream(path)!!.bufferedReader().use { it.readText() }
fun getResourceAsBytes(path: String) =
	object {}.javaClass.getResourceAsStream(path)!!.use { it.readBytes() }
