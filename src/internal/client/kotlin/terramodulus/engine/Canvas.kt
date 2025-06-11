/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.Mui
import terramodulus.engine.ferricia.Mui.clearCanvas
import terramodulus.engine.ferricia.Mui.drawGuiGeo
import terramodulus.engine.ferricia.Mui.drawGuiTex
import terramodulus.engine.ferricia.Mui.dropCanvasHandle
import terramodulus.engine.ferricia.Mui.geoShaders
import terramodulus.engine.ferricia.Mui.getGLVersion
import terramodulus.engine.ferricia.Mui.initCanvasHandle
import terramodulus.engine.ferricia.Mui.loadImageToCanvas
import terramodulus.engine.ferricia.Mui.setCanvasClearColor
import terramodulus.engine.ferricia.Mui.texShaders
import java.io.Closeable

/**
 * Manages the OpenGL viewport rendering as a "**canvas**"; managed by the GL context.
 *
 * This manages GL viewport in the SDL window and rendering in the viewport.
 */
class Canvas internal constructor(private val windowHandle: ULong) : Closeable {
	private val canvasHandle = initCanvasHandle(windowHandle)
	val glVersion = getGLVersion(windowHandle)

	fun clear() = clearCanvas()

	fun setClearColor(r: Float, g: Float, b: Float, a: Float) = setCanvasClearColor(r, g, b, a)

	fun resizeGLViewport() = Mui.resizeGLViewport(windowHandle, canvasHandle)

	fun loadImage(path: String) = loadImageToCanvas(canvasHandle, path)

	fun loadGeoShaders(vsh: String, fsh: String) = geoShaders(vsh, fsh)

	fun loadTexShaders(vsh: String, fsh: String) = texShaders(vsh, fsh)

	fun renderGuiGeo(drawable: GeoDrawable, programHandle: ULong) =
		drawGuiGeo(canvasHandle, drawable.handle, programHandle)

	fun renderGuiTex(drawable: TexDrawable, programHandle: ULong, textureHandle: UInt) =
		drawGuiTex(canvasHandle, drawable.handle, programHandle, textureHandle)

	override fun close() {
		dropCanvasHandle(canvasHandle)
	}
}
