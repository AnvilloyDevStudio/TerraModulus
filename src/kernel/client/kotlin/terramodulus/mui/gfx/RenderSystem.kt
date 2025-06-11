/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.Canvas
import terramodulus.engine.GeoDrawable
import terramodulus.engine.TexDrawable
import java.io.File

private fun getPathOfResource(path: String): String {
	return File(object {}.javaClass.getResource(path)!!.toURI()).absolutePath
}

class RenderSystem internal constructor(private val canvas: Canvas) {
	internal val handle: Handle = HandleImpl()
	private val texShaders = canvas.loadTexShaders(
		getPathOfResource("/gms_tex.vsh"),
		getPathOfResource("/gms_tex.fsh")
	)
	private val geoShaders = canvas.loadGeoShaders(
		getPathOfResource("/gms_geo.vsh"),
		getPathOfResource("/gms_geo.fsh")
	)

	sealed interface Handle {
		fun loadTexture(path: String): UInt
	}

	private inner class HandleImpl : Handle {
		override fun loadTexture(path: String) = canvas.loadImage(getPathOfResource(path))
	}

	internal fun renderGuiTex(drawable: TexDrawable, texture: UInt) = canvas.renderGuiTex(drawable, texShaders, texture)

	internal fun renderGuiGeo(drawable: GeoDrawable) = canvas.renderGuiGeo(drawable, texShaders)

	internal fun render() {

	}
}
