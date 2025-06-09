/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.Canvas
import java.io.File

private fun getPathOfResource(path: String): String {
	return File(object {}.javaClass.getResource(path)!!.toURI()).absolutePath
}

class RenderSystem internal constructor(private val canvas: Canvas) {
	private val texture = canvas.loadImage(getPathOfResource("/test.png"))
	private val shader = canvas.loadShaders(
		getPathOfResource("/gms_tex.vsh"),
		getPathOfResource("/gms_tex.fsh")
	)

	internal fun render() {

	}
}
