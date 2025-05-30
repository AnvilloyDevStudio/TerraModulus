/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

import terramodulus.engine.ferricia.MUI.getGLVersion

/**
 * Manages the OpenGL viewport rendering as a "canvas"; managed by the GL context.
 *
 * This manages GL viewport in the SDL window and rendering in the viewport.
 */
class Canvas internal constructor() {
	val glVersion = getGLVersion()
}
