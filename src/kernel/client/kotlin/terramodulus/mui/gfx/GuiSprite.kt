/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

import terramodulus.engine.SpriteMesh

class GuiSprite(private val rect: RectangleI) {
	private val mesh = SpriteMesh(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height)


}
