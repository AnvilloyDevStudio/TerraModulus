/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.UI.dropSdlHandle
import terramodulus.engine.ferricia.UI.dropWindowHandle
import terramodulus.engine.ferricia.UI.initSdlHandle
import terramodulus.engine.ferricia.UI.initWindowHandle
import java.io.Closeable

class UIManager : Closeable {
	private val sdlHandle = initSdlHandle()
	private val windowHandle = initWindowHandle(sdlHandle)

	override fun close() {
		dropWindowHandle(windowHandle)
		dropSdlHandle(sdlHandle)
	}
}
