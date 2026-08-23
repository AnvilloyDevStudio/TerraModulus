/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine

import com.cout970.math.quaternion.Quatd
import com.cout970.math.vec3.Vec3d
import com.cout970.math.vec4.Vec4i
import net.terramodulus.engine.ferricia.Gwr.newMeshGeomCube
import net.terramodulus.engine.ferricia.Gwr.newMeshGeomSphere
import net.terramodulus.engine.ferricia.Gwr.updateWorldObjModel

sealed class WorldObjDrawable(internal val handle: ULong, private var pos: Vec3d, private var scale: Vec3d, private var rot: Quatd) {
	fun updateModel(px: Double, py: Double, pz: Double, sx: Double, sy: Double, sz: Double, w: Double, i: Double, j: Double, k: Double) =
		updateWorldObjModel(handle, doubleArrayOf(px, py, pz, w, i, j, k, sx, sy, sz))
	fun updateModel(pos: Vec3d, scale: Vec3d, rot: Quatd) =
		updateModel(pos.x, pos.y, pos.z, scale.x, scale.y, scale.z, rot.w, rot.x, rot.y, rot.z)

	init {
		updateModel(pos, scale, rot)
	}

	fun setPos(value: Vec3d) {
		pos = value
		updateModel(pos, scale, rot)
	}

	fun setScale(value: Vec3d) {
		scale = value
		updateModel(pos, scale, rot)
	}

	fun setRot(value: Quatd) {
		rot = value
		updateModel(pos, scale, rot)
	}
}

class SimpleMesh3dGeomCube(canvas: Canvas, width: Float, rgba: Vec4i, pos: Vec3d, scale: Vec3d, rot: Quatd) :
	WorldObjDrawable(canvas.newMeshGeomCube(width, rgba), pos, scale, rot)

class SimpleMesh3dGeomSphere(canvas: Canvas, radius: Float, rgba: Vec4i, pos: Vec3d, scale: Vec3d, rot: Quatd) :
	WorldObjDrawable(canvas.newMeshGeomSphere(radius, rgba), pos, scale, rot)
