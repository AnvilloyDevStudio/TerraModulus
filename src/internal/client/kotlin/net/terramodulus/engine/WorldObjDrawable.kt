/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Gwr.newMeshGeomCube
import net.terramodulus.engine.ferricia.Gwr.newMeshGeomSphere
import net.terramodulus.engine.ferricia.Gwr.updateWorldObjModel

sealed class WorldObjDrawable(internal val handle: ULong, private var pos: Vec3F, private var scale: Vec3F, private var rot: Quat) {
	fun updateModel(px: Float, py: Float, pz: Float, sx: Float, sy: Float, sz: Float, w: Double, i: Double, j: Double, k: Double) =
		updateWorldObjModel(handle, floatArrayOf(px, py, pz, sx, sy, sz), doubleArrayOf(w, i, j, k))
	fun updateModel(pos: Vec3F, scale: Vec3F, rot: Quat) =
		updateModel(pos.x, pos.y, pos.z, scale.x, scale.y, scale.z, rot.w, rot.i, rot.j, rot.k)

	init {
		updateModel(pos, scale, rot)
	}

	fun setPos(value: Vec3F) {
		pos = value
		updateModel(pos, scale, rot)
	}

	fun setScale(value: Vec3F) {
		scale = value
		updateModel(pos, scale, rot)
	}

	fun setRot(value: Quat) {
		rot = value
		updateModel(pos, scale, rot)
	}
}

class SimpleMesh3dGeomCube(width: Float, rgba: Rgba, pos: Vec3F, scale: Vec3F, rot: Quat) :
	WorldObjDrawable(newMeshGeomCube(width, rgba.toArray()), pos, scale, rot)

class SimpleMesh3dGeomSphere(radius: Float, rgba: Rgba, pos: Vec3F, scale: Vec3F, rot: Quat) :
	WorldObjDrawable(newMeshGeomSphere(radius, rgba.toArray()), pos, scale, rot)
