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
	external fun initSdlHandle(): Long;

	/**
	 * @param sdlHandle SDL handle pointer
	 */
	external fun dropSdlHandle(sdlHandle: Long);

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return window handle pointer
	 */
	external fun initWindowHandle(sdlHandle: Long): Long;

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun dropWindowHandle(windowHandle: Long);

	/**
	 * @param windowHandle window handle pointer
	 */
	external fun getGLVersion(windowHandle: Long): String;

	/**
	 * @param sdlHandle SDL handle pointer
	 * @return the list of all MUI events in this frame
	 */
	external fun sdlPoll(sdlHandle: Long): Array<MuiEvent>;
}
