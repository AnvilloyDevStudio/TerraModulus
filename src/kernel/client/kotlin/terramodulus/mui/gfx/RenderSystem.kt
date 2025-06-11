/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.Canvas
import terramodulus.engine.GeomDrawable
import terramodulus.engine.MeshDrawable
import java.io.File

private fun getPathOfResource(path: String): String {
	return File(object {}.javaClass.getResource(path)!!.toURI()).absolutePath
}

class RenderSystem internal constructor(private val canvas: Canvas) {
	val handle: Handle = HandleImpl()
	private val texShaders = canvas.loadTexShaders(
		getPathOfResource("/gms_tex.vsh"),
		getPathOfResource("/gms_tex.fsh")
	)
	private val geoShaders = canvas.loadGeoShaders(
		getPathOfResource("/gms_geo.vsh"),
		getPathOfResource("/gms_geo.fsh")
	)
	val targetFps = 1000;

	sealed interface Handle {
		fun loadTexture(path: String): UInt

		fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float)
	}

	private inner class HandleImpl : Handle {
		override fun loadTexture(path: String) = canvas.loadImage(getPathOfResource(path))

		override fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float) {
			canvas.setClearColor(red, green, blue, alpha)
		}
	}

	internal fun renderGuiTex(drawable: MeshDrawable, texture: UInt) = canvas.renderGuiTex(drawable, texShaders, texture)

	internal fun renderGuiGeo(drawable: GeomDrawable) = canvas.renderGuiGeo(drawable, geoShaders)

	internal fun render() {

	}
}
