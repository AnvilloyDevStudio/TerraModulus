/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Gwr.newMeshGeomCube
import net.terramodulus.engine.ferricia.Gwr.newMeshGeomSphere
import net.terramodulus.engine.ferricia.Gwr.updateWorldObjModel

sealed class WorldObjDrawable(internal val handle: ULong) {
	fun updateModel(px: Float, py: Float, pz: Float, sx: Float, sy: Float, sz: Float, w: Double, i: Double, j: Double, k: Double) =
		updateWorldObjModel(handle, floatArrayOf(px, py, pz, sx, sy, sz), doubleArrayOf(w, i, j, k))
}

class SimpleMesh3dGeomCube(width: Float, r: Int, g: Int, b: Int, a: Int) :
	WorldObjDrawable(newMeshGeomCube(width, intArrayOf(r, g, b, a)))

class SimpleMesh3dGeomSphere(radius: Float, r: Int, g: Int, b: Int, a: Int) :
	WorldObjDrawable(newMeshGeomSphere(radius, intArrayOf(r, g, b, a)))
