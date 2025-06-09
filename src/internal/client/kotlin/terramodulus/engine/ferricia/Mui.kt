/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine.ferricia

import terramodulus.engine.MuiEvent

internal object Mui {
	/**
	 * @return SDL handle pointer
	 */
	external fun initSdlHandle(): ULong

	/**
	 * @param sdlHandle SDL handle pointer
	 */
	external fun dropSdlHandle(sdlHandle: ULong)

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return window handle pointer
	 */
	external fun initWindowHandle(sdlHandle: ULong): ULong

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun dropWindowHandle(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun getGLVersion(windowHandle: ULong): String

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return the list of all MUI events in this frame
	 */
	external fun sdlPoll(sdlHandle: ULong): Array<MuiEvent>

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun resizeGLViewport(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun showWindow(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun swapWindow(windowHandle: ULong)

	/**
	 * @param windowHandle window handle pointer
	 * @return Canvas handle pointer
	 */
	external fun initCanvasHandle(windowHandle: ULong): ULong

	/**
	 * @param canvasHandle Canvas handle pointer
	 */
	external fun dropCanvasHandle(canvasHandle: ULong)

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param path path to RGB image
	 * @return Texture ID
	 */
	@JvmName("loadImageToCanvas")
	external fun loadImageToCanvas(canvasHandle: ULong, path: String): UInt

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
	 * @param canvasHandle Canvas handle pointer
	 * @param shader Shader program ID
	 * @param texture Texture ID
	 */
	@JvmName("renderTexture")
	external fun renderTexture(canvasHandle: ULong, shader: UInt, texture: UInt)

	/**
	 * @param data `[x0, y0, x1, y1, r, g, b, a]`
	 * @return SimpleLineGeom handle pointer
	 */
	@JvmName("newSimpleLineGeom")
	external fun newSimpleLineGeom(data: Array<Int>): ULong

	/**
	 * @param data `[x0, y0, x1, y1]`
	 * @return SpriteMesh as DrawableSet handle pointer
	 */
	@JvmName("newSpriteMesh")
	external fun newSpriteMesh(data: Array<Int>): ULong

	/**
	 * @param data `[w, h]`
	 * @return SmartScaling handle pointer
	 */
	@JvmName("modelSmartScaling")
	external fun modelSmartScaling(data: Array<Int>): ULong

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param modelHandle Model Transform handle pointer
	 */
	@JvmName("addSmartScaling")
	external fun addSmartScaling(drawableHandle: ULong, modelHandle: ULong)

	/**
	 * @param drawableHandle DrawableSet handle pointer
	 * @param modelHandle Model Transform handle pointer
	 */
	@JvmName("removeSmartScaling")
	external fun removeSmartScaling(drawableHandle: ULong, modelHandle: ULong)

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
