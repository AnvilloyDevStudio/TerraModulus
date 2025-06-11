/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine.ferricia

import terramodulus.engine.MuiEvent

@OptIn(ExperimentalUnsignedTypes::class)
internal object Mui {
	/**
	 * @return SDL handle pointer
	 */
	@JvmName("initSdlHandle")
	external fun initSdlHandle(): ULong

	/**
	 * @param sdlHandle SDL handle pointer
	 */
	@JvmName("dropSdlHandle")
	external fun dropSdlHandle(sdlHandle: ULong)

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return window handle pointer
	 */
	@JvmName("initWindowHandle")
	external fun initWindowHandle(sdlHandle: ULong): ULong

	/**
	 * @param windowHandle window handle pointer
	 */
	@JvmName("dropWindowHandle")
	external fun dropWindowHandle(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	@JvmName("getGLVersion")
	external fun getGLVersion(windowHandle: ULong): String

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return the list of all MUI events in this frame
	 */
	@JvmName("sdlPoll")
	external fun sdlPoll(sdlHandle: ULong): Array<MuiEvent>

	/**
	 * @param windowHandle window handle pointer
	 */
	@JvmName("resizeGLViewport")
	external fun resizeGLViewport(windowHandle: ULong, canvasHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	@JvmName("showWindow")
	external fun showWindow(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	@JvmName("swapWindow")
	external fun swapWindow(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 * @return Canvas handle pointer
	 */
	@JvmName("initCanvasHandle")
	external fun initCanvasHandle(windowHandle: ULong): ULong

	/**
	 * @param canvasHandle Canvas handle pointer
	 */
	@JvmName("dropCanvasHandle")
	external fun dropCanvasHandle(canvasHandle: ULong)

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param path path to RGB image
	 * @return Texture ID
	 */
	@JvmName("loadImageToCanvas")
	external fun loadImageToCanvas(canvasHandle: ULong, path: String): UInt

	@JvmName("clearCanvas")
	external fun clearCanvas()

	@JvmName("setCanvasClearColor")
	external fun setCanvasClearColor(r: Float, g: Float, b: Float, a: Float)

	/**
	 * @param vsh path to vector shader
	 * @param fsh path to fragment shader
	 * @return Geo Shader Program handle pointer
	 */
	@JvmName("geoShaders")
	external fun geoShaders(vsh: String, fsh: String): ULong

	/**
	 * @param vsh path to vector shader
	 * @param fsh path to fragment shader
	 * @return Tex Shader Program handle pointer
	 */
	@JvmName("texShaders")
	external fun texShaders(vsh: String, fsh: String): ULong

	/**
	 * @param data `[x0, y0, x1, y1, r, g, b, a]`
	 * @return SimpleLineGeom handle pointer
	 */
	@JvmName("newSimpleLineGeom")
	external fun newSimpleLineGeom(data: IntArray): ULong

	/**
	 * @param data `[x0, y0, x1, y1]`
	 * @return SpriteMesh as DrawableSet handle pointer
	 */
	@JvmName("newSpriteMesh")
	external fun newSpriteMesh(data: IntArray): ULong

	/**
	 * @param data `[w, h, param, w, h]`
	 * @return SmartScaling handle pointers
	 */
	@JvmName("modelSmartScaling")
	external fun modelSmartScaling(data: IntArray): ULongArray

	/**
	 * @param data alpha
	 * @return AlphaFilter handle pointers
	 */
	@JvmName("filterAlphaFilter")
	external fun filterAlphaFilter(data: Float): ULongArray

	/**
	 * @param filter AlphaFilter handle pointer
	 * @param data alpha
	 */
	@JvmName("editAlphaFilter")
	external fun editAlphaFilter(filter: ULong, data: Float)

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param modelHandle Model Transform wide handle pointer
	 */
	@JvmName("addModelTransform")
	external fun addModelTransform(drawableHandle: ULong, modelHandle: ULong)

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param modelHandle Model Transform wide handle pointer
	 */
	@JvmName("removeModelTransform")
	external fun removeModelTransform(drawableHandle: ULong, modelHandle: ULong)

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param filterHandle Color Filter wide handle pointer
	 */
	@JvmName("addColorFilter")
	external fun addColorFilter(drawableHandle: ULong, filterHandle: ULong)

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param filterHandle Color Filter wide handle pointer
	 */
	@JvmName("removeColorFilter")
	external fun removeColorFilter(drawableHandle: ULong, filterHandle: ULong)

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param drawableHandle DrawableSet handle pointer
	 * @param programHandle Geo Shader Program handle pointer
	 */
	@JvmName("drawGuiGeo")
	external fun drawGuiGeo(canvasHandle: ULong, drawableHandle: ULong, programHandle: ULong)

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param drawableHandle DrawableSet handle pointer
	 * @param programHandle Tex Shader Program handle pointer
	 */
	@JvmName("drawGuiTex")
	external fun drawGuiTex(canvasHandle: ULong, drawableHandle: ULong, programHandle: ULong, textureHandle: UInt)
}
