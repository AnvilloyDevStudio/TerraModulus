/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui.gfx

/**
 * Rectangle in a coordinate system with (0, 0) on the bottom left.
 * The anchor of the rectangle is the bottom-left corner.
 */
data class RectangleI(
	val x: Int,
	val y: Int,
	val width: Int,
	val height: Int
) {
	companion object {
		fun withPoints(x0: Int, y0: Int, x1: Int, y1: Int): RectangleI {
			val minX: Int;
			val maxX: Int;
			if (x0 < x1) {
				minX = x0;
				maxX = x1;
			} else {
				maxX = x0;
				minX = x1;
			}
			val minY: Int;
			val maxY: Int;
			if (y0 < y1) {
				minY = y0;
				maxY = y1;
			} else {
				maxY = y0;
				minY = y1;
			}
			return RectangleI(minX, maxX, minY, maxY)
		}
	}

	fun anchor(pos: Anchor5) = when (pos) {
		Anchor5.TopLeft -> Vector2I(x, y + width)
		Anchor5.TopRight -> Vector2I(x + width, y + height)
		Anchor5.BottomLeft -> Vector2I(x, y)
		Anchor5.BottomRight -> Vector2I(x + width, y)
		Anchor5.Center -> Vector2I(x + width / 2, y + height / 2)
	}

	fun translateBy(pos: Vector2I) = RectangleI(x + pos.x, y + pos.y, width, height)

	fun translateBy(x: Int, y: Int) = RectangleI(this.x + x, this.y + y, width, height)

	fun translateByY(y: Int) = RectangleI(x, this.y + y, width, height)

	fun translateByX(x: Int) = RectangleI(this.x + x, y, width, height)

	fun translateToY(y: Int) = RectangleI(x, y, width, height)

	fun translateToX(x: Int) = RectangleI(x, y, width, height)

	fun translateTo(pos: Vector2I) = RectangleI(pos.x, pos.y, width, height)

	fun translateTo(x: Int, y: Int) = RectangleI(x, y, width, height)

	fun toFloat() = RectangleF(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
}

/**
 * Rectangle in a coordinate system with (0, 0) on the bottom left.
 * The anchor of the rectangle is the bottom-left corner.
 */
data class RectangleF(
	val x: Float,
	val y: Float,
	val width: Float,
	val height: Float
) {
	companion object {
		fun withPoints(x0: Float, y0: Float, x1: Float, y1: Float): RectangleF {
			val minX: Float;
			val maxX: Float;
			if (x0 < x1) {
				minX = x0;
				maxX = x1;
			} else {
				maxX = x0;
				minX = x1;
			}
			val minY: Float;
			val maxY: Float;
			if (y0 < y1) {
				minY = y0;
				maxY = y1;
			} else {
				maxY = y0;
				minY = y1;
			}
			return RectangleF(minX, maxX, minY, maxY)
		}
	}

	fun anchor(pos: Anchor5) = when (pos) {
		Anchor5.TopLeft -> Vector2F(x, y + width)
		Anchor5.TopRight -> Vector2F(x + width, y + height)
		Anchor5.BottomLeft -> Vector2F(x, y)
		Anchor5.BottomRight -> Vector2F(x + width, y)
		Anchor5.Center -> Vector2F(x + width / 2, y + height / 2)
	}

	fun translateBy(pos: Vector2F) = RectangleF(x + pos.x, y + pos.y, width, height)

	fun translateBy(x: Float, y: Float) = RectangleF(this.x + x, this.y + y, width, height)

	fun translateByY(y: Float) = RectangleF(x, this.y + y, width, height)

	fun translateByX(x: Float) = RectangleF(this.x + x, y, width, height)

	fun translateToY(y: Float) = RectangleF(x, y, width, height)

	fun translateToX(x: Float) = RectangleF(x, y, width, height)

	fun translateTo(pos: Vector2F) = RectangleF(pos.x, pos.y, width, height)

	fun translateTo(x: Float, y: Float) = RectangleF(x, y, width, height)
}
