/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.dropSdlHandle
import terramodulus.engine.ferricia.Mui.dropWindowHandle
import terramodulus.engine.ferricia.Mui.initSdlHandle
import terramodulus.engine.ferricia.Mui.initWindowHandle
import terramodulus.engine.ferricia.Mui.sdlPoll
import java.io.Closeable

/**
 * Manages the SDL window instance and the underlying GL context.
 */
class Window : Closeable {
	private val sdlHandle = initSdlHandle()
	private val windowHandle = initWindowHandle(sdlHandle)
	val canvas = Canvas(windowHandle)

	fun pollEvents() = sdlPoll(sdlHandle)

	override fun close() {
		dropWindowHandle(windowHandle)
		dropSdlHandle(sdlHandle)
	}
}
