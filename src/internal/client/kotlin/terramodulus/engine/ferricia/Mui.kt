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
	external fun initSdlHandle(): Long

	/**
	 * @param sdlHandle SDL handle pointer
	 */
	external fun dropSdlHandle(sdlHandle: Long)

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return window handle pointer
	 */
	external fun initWindowHandle(sdlHandle: Long): Long

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun dropWindowHandle(windowHandle: Long)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun getGLVersion(windowHandle: Long): String

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return the list of all MUI events in this frame
	 */
	external fun sdlPoll(sdlHandle: Long): Array<MuiEvent>

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun resizeGLViewport(windowHandle: Long)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun showWindow(windowHandle: Long)

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun swapWindow(windowHandle: Long)

	/**
	 * @return Canvas handle pointer
	 */
	external fun initCanvasHandle(): Long

	/**
	 * @param canvasHandle Canvas handle pointer
	 */
	external fun dropCanvasHandle(canvasHandle: Long)

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param path path to RGB image
	 * @return Texture ID
	 */
	@JvmName("loadImageToCanvas")
	external fun loadImageToCanvas(canvasHandle: Long, path: String): UInt

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param vsh path to vector shader
	 * @param fsh path to fragment shader
	 * @return Shader program ID
	 */
	@JvmName("shaders")
	external fun shaders(canvasHandle: Long, vsh: String, fsh: String): UInt

	/**
	 * @param canvasHandle Canvas handle pointer
	 * @param shader Shader program ID
	 * @param texture Texture ID
	 */
	@JvmName("renderTexture")
	external fun renderTexture(canvasHandle: Long, shader: UInt, texture: UInt)
}
