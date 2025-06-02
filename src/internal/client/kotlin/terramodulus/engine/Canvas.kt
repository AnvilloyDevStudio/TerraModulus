/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui.dropCanvasHandle
import terramodulus.engine.ferricia.Mui.getGLVersion
import terramodulus.engine.ferricia.Mui.initCanvasHandle
import terramodulus.engine.ferricia.Mui.loadImageToCanvas
import terramodulus.engine.ferricia.Mui.renderTexture
import terramodulus.engine.ferricia.Mui.shaders
import java.awt.Image
import java.io.Closeable

/**
 * Manages the OpenGL viewport rendering as a "**canvas**"; managed by the GL context.
 *
 * This manages GL viewport in the SDL window and rendering in the viewport.
 */
class Canvas internal constructor(private val windowHandle: Long) : Closeable {
	val glVersion = getGLVersion(windowHandle)
	val canvasHandle = initCanvasHandle()

	fun loadImage(path: String) = loadImageToCanvas(canvasHandle, path)

	fun loadShaders(vsh: String, fsh: String) = shaders(canvasHandle, vsh, fsh)

	fun renderImage(shader: UInt, texture: UInt) = renderTexture(canvasHandle, shader, texture)

	override fun close() {
		dropCanvasHandle(canvasHandle)
	}
}
