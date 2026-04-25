/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gfx

import net.terramodulus.core.TerraModulus
import net.terramodulus.core.getResourceAsBytes
import net.terramodulus.core.getResourceAsString
import net.terramodulus.engine.Canvas
import net.terramodulus.engine.GeomDrawable
import net.terramodulus.engine.MeshDrawable
import net.terramodulus.mui.gms.impl.GameplayScreen

class RenderSystem internal constructor(private val core: TerraModulus, private val canvas: Canvas) {
	val handle: Handle = HandleImpl()
	private val texShaders = canvas.loadTexShaders(
		getResourceAsString("/gms_tex.vsh"),
		getResourceAsString("/gms_tex.fsh")
	)
	private val geoShaders = canvas.loadGeoShaders(
		getResourceAsString("/gms_geo.vsh"),
		getResourceAsString("/gms_geo.fsh")
	)
	val targetFps = 1000;

	sealed interface Handle {
		fun loadTexture(path: String): UInt

		fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float)
	}

	private inner class HandleImpl : Handle {
		override fun loadTexture(path: String) = canvas.loadImage(getResourceAsBytes(path))

		override fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float) {
			canvas.setClearColor(red, green, blue, alpha)
		}
	}

	internal fun newGameplayScreen(pos: Vector3F) =
		{ it: Handle -> GameplayScreen(core, canvas.createCamera(floatArrayOf(pos.x, pos.y, pos.z)), it) }

	internal fun renderGuiTex(drawable: MeshDrawable, texture: UInt) = canvas.renderGuiTex(drawable, texShaders, texture)

	internal fun renderGuiGeo(drawable: GeomDrawable) = canvas.renderGuiGeo(drawable, geoShaders)

	internal fun render() {

	}
}
