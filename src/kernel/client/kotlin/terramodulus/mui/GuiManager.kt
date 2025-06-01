/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.mui

import terramodulus.engine.MuiEvent
import terramodulus.engine.Window
import terramodulus.mui.gms.ScreenManager
import java.io.Closeable

class GuiManager internal constructor() : Closeable {
	private val window = Window()
	private val canvas = window.canvas
	val renderSystem = RenderSystem()
	val inputSystem = InputSystem()
	val screenManager = ScreenManager()

	/**
	 * Screen updating, targeting twice as *maximum FPS*.
	 */
	internal fun updateScreen() {}

	/**
	 * Canvas updating, per frame, maximally the *maximum FPS*.
	 * This includes input ticking and canvas rendering.
	 */
	internal fun updateCanvas() {
		window.pollEvents().forEach { event ->
			when (event) {
				is MuiEvent.DisplayAdded -> TODO()
				is MuiEvent.DisplayMoved -> TODO()
				is MuiEvent.DisplayRemoved -> TODO()
				MuiEvent.DropBegin -> TODO()
				MuiEvent.DropComplete -> TODO()
				is MuiEvent.DropFile -> TODO()
				MuiEvent.DropPosition -> TODO()
				is MuiEvent.DropText -> TODO()
				is MuiEvent.GamepadAdded -> TODO()
				is MuiEvent.GamepadAxisMotion -> TODO()
				is MuiEvent.GamepadButtonDown -> TODO()
				is MuiEvent.GamepadButtonUp -> TODO()
				is MuiEvent.GamepadRemapped -> TODO()
				is MuiEvent.GamepadRemoved -> TODO()
				MuiEvent.GamepadSteamHandleUpdated -> TODO()
				is MuiEvent.GamepadTouchpadDown -> TODO()
				is MuiEvent.GamepadTouchpadMotion -> TODO()
				is MuiEvent.GamepadTouchpadUp -> TODO()
				is MuiEvent.JoystickAdded -> TODO()
				is MuiEvent.JoystickAxisMotion -> TODO()
				MuiEvent.JoystickBallMotion -> TODO()
				MuiEvent.JoystickBatteryUpdated -> TODO()
				is MuiEvent.JoystickButtonDown -> TODO()
				is MuiEvent.JoystickButtonUp -> TODO()
				is MuiEvent.JoystickHatMotion -> TODO()
				is MuiEvent.JoystickRemoved -> TODO()
				MuiEvent.KeyboardAdded -> TODO()
				is MuiEvent.KeyboardKeyDown -> TODO()
				is MuiEvent.KeyboardKeyUp -> TODO()
				MuiEvent.KeyboardRemoved -> TODO()
				MuiEvent.KeymapChanged -> TODO()
				MuiEvent.MouseAdded -> TODO()
				is MuiEvent.MouseButtonDown -> TODO()
				is MuiEvent.MouseButtonUp -> TODO()
				is MuiEvent.MouseMotion -> TODO()
				MuiEvent.MouseRemoved -> TODO()
				is MuiEvent.MouseWheel -> TODO()
				MuiEvent.RenderDeviceLost -> TODO()
				MuiEvent.RenderDeviceReset -> TODO()
				MuiEvent.RenderTargetsReset -> TODO()
				is MuiEvent.TextEditing -> TODO()
				MuiEvent.TextEditingCandidates -> TODO()
				is MuiEvent.TextInput -> TODO()
				MuiEvent.WindowCloseRequested -> TODO()
				MuiEvent.WindowDestroyed -> TODO()
				MuiEvent.WindowEnterFullscreen -> TODO()
				MuiEvent.WindowExposed -> TODO()
				MuiEvent.WindowFocusGained -> TODO()
				MuiEvent.WindowFocusLost -> TODO()
				MuiEvent.WindowHdrStateChanged -> TODO()
				MuiEvent.WindowHidden -> TODO()
				MuiEvent.WindowIccProfChanged -> TODO()
				MuiEvent.WindowLeaveFullscreen -> TODO()
				MuiEvent.WindowMaximized -> TODO()
				MuiEvent.WindowMetalViewResized -> TODO()
				MuiEvent.WindowMinimized -> TODO()
				MuiEvent.WindowMouseEnter -> TODO()
				MuiEvent.WindowMouseLeave -> TODO()
				is MuiEvent.WindowMoved -> TODO()
				MuiEvent.WindowOccluded -> TODO()
				is MuiEvent.WindowPixelSizeChanged -> TODO()
				is MuiEvent.WindowResized -> TODO()
				MuiEvent.WindowRestored -> TODO()
				MuiEvent.WindowShown -> TODO()
			}
		}
	}

	override fun close() {
		window.close()
	}
}
