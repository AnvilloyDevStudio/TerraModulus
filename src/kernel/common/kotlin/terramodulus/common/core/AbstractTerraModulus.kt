/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.common.core

import java.io.Closeable

abstract class AbstractTerraModulus : Closeable {
	abstract var tps: Int

	abstract fun run()
}
