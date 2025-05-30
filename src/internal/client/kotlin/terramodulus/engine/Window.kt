/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.MUI.dropSdlHandle
import terramodulus.engine.ferricia.MUI.dropWindowHandle
import terramodulus.engine.ferricia.MUI.initSdlHandle
import terramodulus.engine.ferricia.MUI.initWindowHandle
import java.io.Closeable

/**
 * Manages the SDL window instance and the underlying GL context.
 */
class Window : Closeable {
	private val sdlHandle = initSdlHandle()
	private val windowHandle = initWindowHandle(sdlHandle)
	val canvas = Canvas()

	override fun close() {
		dropWindowHandle(windowHandle)
		dropSdlHandle(sdlHandle)
	}
}
