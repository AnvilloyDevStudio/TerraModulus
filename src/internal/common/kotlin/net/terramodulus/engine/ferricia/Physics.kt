/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.engine.ferricia

@OptIn(ExperimentalUnsignedTypes::class)
internal object Physics {
	/**
	 * @return PhyEnv pointer
	 */
	@JvmName("newPhyEnv")
	external fun newPhyEnv(): ULong

	@JvmName("dropPhyEnv")
	external fun dropPhyEnv(handle: ULong)

	/**
	 * @param handle PhyEnv pointer
	 * @return PhyWorld pointer
	 */
	@JvmName("newPhyWorld")
	external fun newPhyWorld(handle: ULong): ULong

	/**
	 * @param handle PhyWorld pointer
	 * @param lengths x, y, z lengths
	 * @return PhyRawGeomPlaceable pointer
	 */
	@JvmName("newWorldPhyGeomBox")
	external fun newWorldPhyGeomBox(handle: ULong, lengths: DoubleArray): ULong

	/**
	 * @param handle PhyWorld pointer
	 * @return PhyRawGeomPlaceable pointer
	 */
	@JvmName("newWorldPhyGeomSphere")
	external fun newWorldPhyGeomSphere(handle: ULong, radius: Double): ULong

	/**
	 * @param handle PhyRawGeomPlaceable pointer
	 * @param pos x, y, z position
	 */
	@JvmName("setPhyRawGeomPlaceablePosition")
	external fun setPhyRawGeomPlaceablePosition(handle: ULong, pos: DoubleArray)

	/**
	 * @param handle PhyRawGeomPlaceable pointer
	 * @return x, y, z position
	 */
	@JvmName("getPhyRawGeomPlaceablePosition")
	external fun getPhyRawGeomPlaceablePosition(handle: ULong): DoubleArray

}
