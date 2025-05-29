/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine.ferricia

object UI {
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
}
