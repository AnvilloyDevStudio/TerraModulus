/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.util.logging

import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.appender.MemoryMappedFileAppender
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.Node
import org.apache.logging.log4j.core.config.Property
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAttribute
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration
import org.apache.logging.log4j.core.config.plugins.PluginElement
import org.apache.logging.log4j.core.config.plugins.PluginFactory
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required
import java.io.Serializable
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * A wrapper over the underlying [MemoryMappedFileAppender] with the interoperation with single-instance checking.
 *
 * Note that this is not ready for generic public utility interface since some bridges are ignored
 * due to the internal usage of this [Appender]. This includes features related to asynchronicity and filters.
 */
@Suppress("unused")
@Plugin(name = "DelayedFile", category = Node.CATEGORY, elementType = Appender.ELEMENT_TYPE, printObject = true)
internal class DelayedFileAppender private constructor(
	name: String,
	layout: Layout<out Serializable>?,
	config: Configuration,
) : AbstractAppender(name, null, layout, true, Property.EMPTY_ARRAY) {
	private var session: Session = Session.Initial(DataFactory(name, layout, config))

	init {
		State.appender()
	}

	/**
	 * With bridges of Life Cycle from
	 * [AbstractOutputStreamAppender][org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender]
	 */
	private sealed interface Session {
		/** @see org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.start */
		fun start()

		/** @see org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.stop */
		fun stop(timeout: Long, timeUnit: TimeUnit?): Boolean

		/** @see org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.append */
		fun append(event: LogEvent)

		/**
		 * The initial state of the appender, which has not been initialized for the process.
		 *
		 * Life cycle is not applicable here, so NO-OP is applied for relevant functions.
		 * All [LogEvent] instances passed to this appender are stored temporary until the
		 * appender is initialized, and all stored events would be passed immediately afterward.
		 */
		class Initial(val data: DataFactory) : Session {
			private val events: MutableList<LogEvent> = ArrayList()

			fun buildFinal(): Final {
				val timestamp = Date(ManagementFactory.getRuntimeMXBean().startTime)
				val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS")
				val final = Final(MemoryMappedFileAppender.Builder()
					.setName(data.name)
					.setLayout(data.layout)
					.setConfiguration(data.config)
					.setFileName("${format.format(timestamp)}.log")
					.build())
				events.forEach(final::append)
				return final
			}

			override fun start() {}

			override fun stop(timeout: Long, timeUnit: TimeUnit?): Boolean = true

			override fun append(event: LogEvent) {
				events.add(event)
			}
		}

		/** The initialized state of the appender, ready for use. */
		class Final(val fileAppender: MemoryMappedFileAppender) : Session {
			override fun start() = fileAppender.start()

			override fun stop(timeout: Long, timeUnit: TimeUnit?): Boolean = fileAppender.stop(timeout, timeUnit)

			override fun append(event: LogEvent) = fileAppender.append(event)
		}
	}

	private class DataFactory(
		val name: String,
		val layout: Layout<out Serializable>?,
		val config: Configuration,
	)

	companion object {
		@PluginFactory
		@JvmStatic
		fun createAppender(
			@PluginConfiguration config: Configuration,
			@PluginAttribute("name") @Required name: String,
			@PluginElement("Layout") layout: Layout<out Serializable>?,
		): Appender = DelayedFileAppender(name, layout, config)
	}

	override fun start() = super.start()

	override fun stop(timeout: Long, timeUnit: TimeUnit?): Boolean = super.stop(timeout, timeUnit)

	override fun append(event: LogEvent) = session.append(event)
}
