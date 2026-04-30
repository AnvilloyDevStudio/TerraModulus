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
class Window(
	width: UInt,
	height: UInt,
) : Closeable {
	var width = width
		private set
	var height = height
		private set
	private val sdlHandle = initSdlHandle()
	private val windowHandle = initWindowHandle(sdlHandle) // TODO pass dimensions
	val canvas = Canvas(windowHandle)

	fun sizeChanged(width: UInt, height: UInt) {
		this.width = width
		this.height = height
		canvas.resizeGLViewport()
	}

	fun show() = showWindow(windowHandle)

	fun swap() = swapWindow(windowHandle)

	fun pollEvents() = sdlPoll(sdlHandle)

	override fun close() {
		dropWindowHandle(windowHandle)
		dropSdlHandle(sdlHandle)
	}
}
