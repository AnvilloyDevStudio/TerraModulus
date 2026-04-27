/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import net.terramodulus.engine.ferricia.Gwr.newMeshGeomCube
import net.terramodulus.engine.ferricia.Gwr.newMeshGeomSphere
import net.terramodulus.engine.ferricia.Gwr.updateWorldObjModel

sealed class WorldObjDrawable(internal val handle: ULong, private var pos: Vec3D, private var scale: Vec3D, private var rot: Quat) {
	fun updateModel(px: Double, py: Double, pz: Double, sx: Double, sy: Double, sz: Double, w: Double, i: Double, j: Double, k: Double) =
		updateWorldObjModel(handle, doubleArrayOf(px, py, pz, w, i, j, k, sx, sy, sz))
	fun updateModel(pos: Vec3D, scale: Vec3D, rot: Quat) =
		updateModel(pos.x, pos.y, pos.z, scale.x, scale.y, scale.z, rot.w, rot.i, rot.j, rot.k)

	init {
		updateModel(pos, scale, rot)
	}

	fun setPos(value: Vec3D) {
		pos = value
		updateModel(pos, scale, rot)
	}

	fun setScale(value: Vec3D) {
		scale = value
		updateModel(pos, scale, rot)
	}

	fun setRot(value: Quat) {
		rot = value
		updateModel(pos, scale, rot)
	}
}

class SimpleMesh3dGeomCube(width: Float, rgba: Rgba, pos: Vec3D, scale: Vec3D, rot: Quat) :
	WorldObjDrawable(newMeshGeomCube(width, rgba.toArray()), pos, scale, rot)

class SimpleMesh3dGeomSphere(radius: Float, rgba: Rgba, pos: Vec3D, scale: Vec3D, rot: Quat) :
	WorldObjDrawable(newMeshGeomSphere(radius, rgba.toArray()), pos, scale, rot)
