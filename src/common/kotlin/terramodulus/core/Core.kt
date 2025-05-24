/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.core

import terramodulus.util.logging.logger
import java.io.Closeable
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Files
import java.nio.file.Path

const val NAME = "TerraModulus"
const val VERSION = "0.1.0" // TODO placeholder

private val logger = logger {}

/** Checks whether this instance is the first process launched in the game data directory. */
fun checkSingleInstance(dir: Path) {
	if (dir.parent != null) Files.createDirectories(dir.parent)
	val lockFile = File(dir.toFile(), "session.lock")
	if (lockFile.createNewFile()) {
		logger.info { "LOCK file is created." }
	} else {
		logger.info { "LOCK file exists." }
	}

	SessionLockFile(RandomAccessFile(lockFile, "rw"))
}

/**
 * @throws java.io.IOException
 */
class SessionLockFile internal constructor(private val file: RandomAccessFile) : Closeable {
	private val channel: FileChannel = file.channel
	private val lock: FileLock = channel.lock()

	/**
	 * @throws java.io.IOException
	 */
	override fun close() {
		lock.close()
		channel.close()
		file.close()
	}
}
