/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Gwr.drawGwrObj
import net.terramodulus.engine.ferricia.Gwr.geoShaders
import net.terramodulus.engine.ferricia.Gwr.newCamera
import net.terramodulus.engine.ferricia.Gwr.refreshCameraPos
import net.terramodulus.engine.ferricia.Gwr.setCameraZoomLevel
import java.io.Closeable

class Camera3D internal constructor(private val canvas: Canvas, pos: FloatArray) : Closeable {
	internal val handle = newCamera(canvas.handle, pos)

	fun loadGeoShaders(vsh: String, fsh: String) = geoShaders(vsh, fsh)

	fun refreshPos(pos: FloatArray) = refreshCameraPos(handle, pos)

	fun setZoomLevel(zoomLevel: Float) = setCameraZoomLevel(handle, zoomLevel)

	fun renderGwrGeo(drawable: WorldObjDrawable, programHandle: ULong) =
		drawGwrObj(canvas.handle, handle, drawable.handle, programHandle)

	override fun close() {
		canvas.camera3D = null
	}
}
