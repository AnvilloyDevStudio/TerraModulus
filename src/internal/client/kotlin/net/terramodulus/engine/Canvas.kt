/*
 * SPDX-FileCopyrightText: 2025-2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Mui
import net.terramodulus.engine.ferricia.Mui.clearCanvas
import net.terramodulus.engine.ferricia.Mui.drawGuiGeo
import net.terramodulus.engine.ferricia.Mui.drawGuiTex
import net.terramodulus.engine.ferricia.Mui.dropCanvasHandle
import net.terramodulus.engine.ferricia.Mui.geoShaders
import net.terramodulus.engine.ferricia.Mui.getGLVersion
import net.terramodulus.engine.ferricia.Mui.initCanvasHandle
import net.terramodulus.engine.ferricia.Mui.loadImageToCanvas
import net.terramodulus.engine.ferricia.Mui.setCanvasClearColor
import net.terramodulus.engine.ferricia.Mui.texShaders
import java.io.Closeable

/**
 * Manages the OpenGL viewport rendering as a "**canvas**"; managed by the GL context.
 *
 * This manages GL viewport in the SDL window and rendering in the viewport.
 */
class Canvas internal constructor(private val windowHandle: ULong) : Closeable {
	internal val handle = initCanvasHandle(windowHandle)
	val glVersion = getGLVersion(windowHandle)
	internal var camera3D: Camera3D? = null;

	fun clear() = clearCanvas()

	fun setClearColor(r: Float, g: Float, b: Float, a: Float) = setCanvasClearColor(r, g, b, a)

	fun resizeGLViewport() = if (camera3D == null) {
		Mui.resizeGLViewport(windowHandle, handle)
	} else {
		Mui.resizeGLViewportCamera(windowHandle, handle, camera3D!!.handle)
	}

	fun createCamera(pos: FloatArray): Camera3D {
		camera3D = Camera3D(this, pos)
		return camera3D!!
	}

	fun loadImage(data: ByteArray) = loadImageToCanvas(handle, data)

	fun loadGeoShaders(vsh: String, fsh: String) = geoShaders(vsh, fsh)

	fun loadTexShaders(vsh: String, fsh: String) = texShaders(vsh, fsh)

	fun renderGuiGeo(drawable: GeomDrawable, programHandle: ULong) =
		drawGuiGeo(handle, drawable.handle, programHandle)

	fun renderGuiTex(drawable: MeshDrawable, programHandle: ULong, textureHandle: UInt) =
		drawGuiTex(handle, drawable.handle, programHandle, textureHandle)

	override fun close() {
		dropCanvasHandle(handle)
	}
}
