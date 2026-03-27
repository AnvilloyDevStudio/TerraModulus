/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Mui.dropSdlHandle
import net.terramodulus.engine.ferricia.Mui.dropWindowHandle
import net.terramodulus.engine.ferricia.Mui.initSdlHandle
import net.terramodulus.engine.ferricia.Mui.initWindowHandle
import net.terramodulus.engine.ferricia.Mui.resizeGLViewport
import net.terramodulus.engine.ferricia.Mui.sdlPoll
import net.terramodulus.engine.ferricia.Mui.showWindow
import net.terramodulus.engine.ferricia.Mui.swapWindow
import java.io.Closeable

/**
 * Manages the SDL window instance and the underlying GL context.
 */
class Window : Closeable {
	private val sdlHandle = initSdlHandle()
	private val windowHandle = initWindowHandle(sdlHandle)
	val canvas = Canvas(windowHandle)

	fun show() = showWindow(windowHandle)

	fun swap() = swapWindow(windowHandle)

	fun pollEvents() = sdlPoll(sdlHandle)

	override fun close() {
		dropWindowHandle(windowHandle)
		dropSdlHandle(sdlHandle)
	}
}
