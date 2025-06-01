/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package terramodulus.engine

sealed interface MuiEvent {
	data class DisplayAdded(val displayHandle: Long) : MuiEvent
	data class DisplayRemoved(val displayHandle: Long) : MuiEvent
	data class DisplayMoved(val displayHandle: Long) : MuiEvent
	data object WindowShown : MuiEvent
	data object WindowHidden : MuiEvent
	data object WindowExposed : MuiEvent
	data class WindowMoved(val x: Int, val y: Int) : MuiEvent
	data class WindowResized(val width: UInt, val height: UInt) : MuiEvent
	data class WindowPixelSizeChanged(val width: UInt, val height: UInt) : MuiEvent
	data object WindowMetalViewResized : MuiEvent
	data object WindowMinimized : MuiEvent
	data object WindowMaximized : MuiEvent
	data object WindowRestored : MuiEvent
	data object WindowMouseEnter : MuiEvent
	data object WindowMouseLeave : MuiEvent
	data object WindowFocusGained : MuiEvent
	data object WindowFocusLost : MuiEvent
	data object WindowCloseRequested : MuiEvent
	data object WindowIccProfChanged : MuiEvent
	data object WindowOccluded : MuiEvent
	data object WindowEnterFullscreen : MuiEvent
	data object WindowLeaveFullscreen : MuiEvent
	data object WindowDestroyed : MuiEvent
	data object WindowHdrStateChanged : MuiEvent
	data class KeyboardKeyDown(val keyboardId: UInt, val key: UInt) : MuiEvent
	data class KeyboardKeyUp(val keyboardId: UInt, val key: UInt) : MuiEvent
	data class TextEditing(val text: String, val start: Int, val length: UInt) : MuiEvent
	data class TextInput(val text: String) : MuiEvent
	data object KeymapChanged : MuiEvent
	data object KeyboardAdded : MuiEvent
	data object KeyboardRemoved : MuiEvent
	data object TextEditingCandidates : MuiEvent
	data class MouseMotion(val mouseId: UInt, val x: Float, val y: Float) : MuiEvent
	data class MouseButtonDown(val mouseId: UInt, val key: UByte) : MuiEvent
	data class MouseButtonUp(val mouseId: UInt, val key: UByte) : MuiEvent
	data class MouseWheel(val mouseId: UInt, val x: Float, val y: Float) : MuiEvent
	data object MouseAdded : MuiEvent
	data object MouseRemoved : MuiEvent
	data class JoystickAxisMotion(val joystickId: UInt, val axis: UByte, val value: Short) : MuiEvent
	data object JoystickBallMotion : MuiEvent
	data class JoystickHatMotion(val joystickId: UInt, val hat: UByte, val value: UByte) : MuiEvent
	data class JoystickButtonDown(val joystickId: UInt, val button: UByte) : MuiEvent
	data class JoystickButtonUp(val joystickId: UInt, val button: UByte) : MuiEvent
	data class JoystickAdded(val joystickId: UInt) : MuiEvent
	data class JoystickRemoved(val joystickId: UInt) : MuiEvent
	data object JoystickBatteryUpdated : MuiEvent
	data class GamepadAxisMotion(val joystickId: UInt, val axis: UByte, val value: Short) : MuiEvent
	data class GamepadButtonDown(val joystickId: UInt, val button: UByte) : MuiEvent
	data class GamepadButtonUp(val joystickId: UInt, val button: UByte) : MuiEvent
	data class GamepadAdded(val joystickId: UInt) : MuiEvent
	data class GamepadRemoved(val joystickId: UInt) : MuiEvent
	data class GamepadRemapped(val joystickId: UInt) : MuiEvent
	data class GamepadTouchpadDown(val joystickId: UInt, val touchpad: Int, val finger: Int,
	                               val x: Float, val y: Float, val pressure: Float) : MuiEvent
	data class GamepadTouchpadMotion(val joystickId: UInt, val touchpad: Int, val finger: Int,
	                                 val x: Float, val y: Float, val pressure: Float) : MuiEvent
	data class GamepadTouchpadUp(val joystickId: UInt, val touchpad: Int, val finger: Int,
	                             val x: Float, val y: Float, val pressure: Float) : MuiEvent
	data object GamepadSteamHandleUpdated : MuiEvent
	data class DropFile(val filename: String) : MuiEvent
	data class DropText(val text: String) : MuiEvent
	data object DropBegin : MuiEvent
	data object DropComplete : MuiEvent
	data object DropPosition : MuiEvent
	data object RenderTargetsReset : MuiEvent
	data object RenderDeviceReset : MuiEvent
	data object RenderDeviceLost : MuiEvent
}
