/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.common.core

import joptsimple.OptionException
import net.terramodulus.core.NAME
import net.terramodulus.core.VERSION
import net.terramodulus.engine.initEngine
import net.terramodulus.util.exception.Error
import net.terramodulus.util.exception.Fault
import net.terramodulus.util.exception.UnhandledExceptionFault
import net.terramodulus.util.exception.triggerGlobalCrash
import net.terramodulus.util.logging.initLogging
import net.terramodulus.util.logging.logger
import java.io.Closeable
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Files
import java.nio.file.Path

private val logger = run {
	initLogging()
	logger {}
}

/**
 * This should only be used by `terramodulus.core.Main` in the beginning of `main` function.
 */
fun setupInit() {
	Thread.setDefaultUncaughtExceptionHandler { _, e ->
		triggerGlobalCrash(UnhandledExceptionFault.global(e))
	}

	logger.info {"System Properties:\n${
		System.getProperties().entries.joinToString(separator = "\n") {
			"\t${it.key}=${it.value}"
		}
	}"}

	initEngine()
}

fun run(instance: AbstractTerraModulus) {
	logger.info { "==== $NAME $VERSION ====" }
	try {
		instance.run()
	} catch (e: Throwable) {
		triggerGlobalCrash(UnhandledExceptionFault.scoped("instance.run()", e))
	} finally {
	    instance.close()
	}
}

/**
 * This should only be used by `terramodulus.core.Main`.
 */
class ApplicationInitializationFault(cause: Throwable) :
	Fault("Failed to initialize application", cause)

class ApplicationArgumentParsingError(cause: OptionException) :
	Error("Failed to parse application arguments", cause)

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
