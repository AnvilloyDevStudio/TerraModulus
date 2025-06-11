/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui

import terramodulus.engine.MuiEvent
import terramodulus.engine.Window
import terramodulus.mui.gfx.RenderSystem
import terramodulus.mui.gms.ScreenManager
import terramodulus.mui.input.InputSystem
import terramodulus.util.logging.logger
import java.io.Closeable
import java.io.File

private val logger = logger {}

/**
 * Graphical User Interface (GUI) Manager
 */
internal class GuiManager internal constructor() : Closeable {
	private val window = Window()
	val renderSystem = RenderSystem(window.canvas)
	val inputSystem = InputSystem()
	val screenManager = ScreenManager(renderSystem.handle)

	internal fun showWindow() = window.show()

// 	/**
// 	 * Screen updating, targeting as the same as *maximum FPS*,
// 	 * but the numbers of ticks are not supposed to be compensated when missed,
// 	 * so it is up to the callers to compensate missed activities.
// 	 */
// 	internal fun updateScreens() {}

	/**
	 * Canvas updating, per frame, maximally the *maximum FPS*.
	 * This includes input ticking and canvas rendering.
	 */
	internal fun updateCanvas() {
		window.pollEvents().forEach { event ->
			when (event) {
				is MuiEvent.DisplayAdded -> {
					logger.debug { "Display added." }
				}
				is MuiEvent.DisplayMoved -> {
					logger.debug { "Display moved." }
				}
				is MuiEvent.DisplayRemoved -> {
					logger.debug { "Display removed." }
				}
				MuiEvent.DropBegin -> {
					logger.debug { "Drop began." }
				}
				MuiEvent.DropComplete -> {
					logger.debug { "Drop completed." }
				}
				is MuiEvent.DropFile -> {
					logger.debug { "Dropped file \"${event.filename}\" on window." }
				}
				MuiEvent.DropPosition -> {
					logger.debug { "Drop position." }
				}
				is MuiEvent.DropText -> {
					logger.debug { "Dropped text \"${event.text}\" on window." }
				}
				is MuiEvent.GamepadAdded -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) added." }
				}
				is MuiEvent.GamepadAxisMotion -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) axis (${event.axis}) motion: ${event.value}" }
				}
				is MuiEvent.GamepadButtonDown -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) button (${event.button}) down." }
				}
				is MuiEvent.GamepadButtonUp -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) button (${event.button}) up." }
				}
				is MuiEvent.GamepadRemapped -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) remapped." }
				}
				is MuiEvent.GamepadRemoved -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) removed." }
				}
				MuiEvent.GamepadSteamHandleUpdated -> {
					logger.debug { "Gamepad steam handle updated." }
				}
				is MuiEvent.GamepadTouchpadDown -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) touchpad (${event.touchpad}) down." }
				}
				is MuiEvent.GamepadTouchpadMotion -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) touchpad (${event.touchpad}) motion." }
				}
				is MuiEvent.GamepadTouchpadUp -> {
					logger.debug { "Gamepad (id: ${event.joystickId}) touchpad (${event.touchpad}) up." }
				}
				is MuiEvent.JoystickAdded -> {
					logger.debug { "Joystick (id: ${event.joystickId}) added." }
				}
				is MuiEvent.JoystickAxisMotion -> {
					logger.debug { "Joystick (id: ${event.joystickId}) axis (${event.axis}) motion: ${event.value}" }
				}
				MuiEvent.JoystickBallMotion -> {
					logger.debug { "Joystick ball motion." }
				}
				MuiEvent.JoystickBatteryUpdated -> {
					logger.debug { "Joystick battery updated." }
				}
				is MuiEvent.JoystickButtonDown -> {
					logger.debug { "Joystick (id: ${event.joystickId}) button (${event.button}) down." }
				}
				is MuiEvent.JoystickButtonUp -> {
					logger.debug { "Joystick (id: ${event.joystickId}) button (${event.button}) up." }
				}
				is MuiEvent.JoystickHatMotion -> {
					logger.debug { "Joystick (id: ${event.joystickId}) hat (${event.hat}) motion: ${event.value}." }
				}
				is MuiEvent.JoystickRemoved -> {
					logger.debug { "Joystick (id: ${event.joystickId}) removed." }
				}
				MuiEvent.KeyboardAdded -> {
					logger.debug { "Keyboard added." }
				}
				is MuiEvent.KeyboardKeyDown -> {
					logger.debug { "Keyboard (id: ${event.keyboardId}) key `${event.key}` down." }
				}
				is MuiEvent.KeyboardKeyUp -> {
					logger.debug { "Keyboard (id: ${event.keyboardId}) key `${event.key}` up." }
				}
				MuiEvent.KeyboardRemoved -> {
					logger.debug { "Keyboard removed." }
				}
				MuiEvent.KeymapChanged -> {
					logger.debug { "Keyboard keymap changed." }
				}
				MuiEvent.MouseAdded -> {
					logger.debug { "Mouse added." }
				}
				is MuiEvent.MouseButtonDown -> {
					logger.debug { "Mouse (id: ${event.mouseId}) key `${event.key}` down." }
				}
				is MuiEvent.MouseButtonUp -> {
					logger.debug { "Mouse (id: ${event.mouseId}) key `${event.key}` up." }
				}
				is MuiEvent.MouseMotion -> {
					logger.debug { "Mouse (id: ${event.mouseId}) motion (${event.x}, ${event.y})." }
				}
				MuiEvent.MouseRemoved -> {
					logger.debug { "Mouse removed." }
				}
				is MuiEvent.MouseWheel -> {
					logger.debug { "Mouse (id: ${event.mouseId}) wheel (${event.x}, ${event.y})." }
				}
				MuiEvent.RenderDeviceLost -> {
					logger.debug { "Render device lost." }
				}
				MuiEvent.RenderDeviceReset -> {
					logger.debug { "Render device reset." }
				}
				MuiEvent.RenderTargetsReset -> {
					logger.debug { "Render targets reset." }
				}
				is MuiEvent.TextEditing -> {
					logger.debug { "Text editing at ${event.start} with length ${event.length}." }
				}
				MuiEvent.TextEditingCandidates -> {
					logger.debug { "Text editing candidates." }
				}
				is MuiEvent.TextInput -> {
					logger.debug { "Text input." }
				}
				MuiEvent.WindowCloseRequested -> {
					logger.debug { "Window close requested." }
				}
				MuiEvent.WindowDestroyed -> {
					logger.debug { "Window destroyed." }
				}
				MuiEvent.WindowEnterFullscreen -> {
					logger.debug { "Window entered fullscreen." }
				}
				MuiEvent.WindowExposed -> {
					logger.debug { "Window exposed." }
				}
				MuiEvent.WindowFocusGained -> {
					logger.debug { "Window focus gained." }
				}
				MuiEvent.WindowFocusLost -> {
					logger.debug { "Window focus lost." }
				}
				MuiEvent.WindowHdrStateChanged -> {
					logger.debug { "Window HDR state changed." }
				}
				MuiEvent.WindowHidden -> {
					logger.debug { "Window hidden." }
				}
				MuiEvent.WindowIccProfChanged -> {
					logger.debug { "Window ICC profile changed." }
				}
				MuiEvent.WindowLeaveFullscreen -> {
					logger.debug { "Window left fullscreen." }
				}
				MuiEvent.WindowMaximized -> {
					logger.debug { "Window maximized." }
				}
				MuiEvent.WindowMetalViewResized -> {
					logger.debug { "Window metal view resized." }
				}
				MuiEvent.WindowMinimized -> {
					logger.debug { "Window minimized." }
				}
				MuiEvent.WindowMouseEnter -> {
					logger.debug { "Mouse entered window." }
				}
				MuiEvent.WindowMouseLeave -> {
					logger.debug { "Mouse left window." }
				}
				is MuiEvent.WindowMoved -> {
					logger.debug { "Window moved to (${event.x}, ${event.y})." }
				}
				MuiEvent.WindowOccluded -> {
					logger.debug { "Window occluded." }
				}
				is MuiEvent.WindowPixelSizeChanged -> {
					logger.debug { "Window pixel size changed to ${event.width}x${event.height}." }
					window.canvas.resizeGLViewport()
					logger.debug { "Window viewport resized." }
				}
				is MuiEvent.WindowResized -> {
					logger.debug { "Window resized to ${event.width}x${event.height}." }
				}
				MuiEvent.WindowRestored -> {
					logger.debug { "Window restored." }
				}
				MuiEvent.WindowShown -> {
					logger.debug { "Window shown." }
				}
			}
		}
		window.canvas.clear()
		screenManager.render(renderSystem)
		window.swap()
	}

	override fun close() {
		window.close()
	}
}
