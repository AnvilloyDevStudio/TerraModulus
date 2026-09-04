/*
 * SPDX-FileCopyrightText: 2025 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.kui

// TODO Temporary solution, should be rewritten in next update.
class InputSystem internal constructor() {
	private val keys = HashMap<KeyId, Key>()

	sealed interface Keys {
		fun down(): KeyPredicate.Down
		fun justDown(): KeyPredicate.JustDown
		fun justUp(): KeyPredicate.JustUp
		fun matches(other: KeyId): Boolean
	}

	private class KeysImpl(private val id: KeyId) : Keys {
		override fun down() = KeyPredicate.Down(id)
		override fun justDown() = KeyPredicate.JustDown(id)
		override fun justUp() = KeyPredicate.JustUp(id)
		override fun matches(other: KeyId) = id == other
	}

	// Values refer to ferricia::mui::KeyboardKey
	enum class KeyboardKeys(private val id: KeyId) : Keys by KeysImpl(id) {
		A(KeyId(0u)),
		B(KeyId(1u)),
		C(KeyId(2u)),
		D(KeyId(3u)),
		E(KeyId(4u)),
		F(KeyId(5u)),
		G(KeyId(6u)),
		H(KeyId(7u)),
		I(KeyId(8u)),
		J(KeyId(9u)),
		K(KeyId(10u)),
		L(KeyId(11u)),
		M(KeyId(12u)),
		N(KeyId(13u)),
		O(KeyId(14u)),
		P(KeyId(15u)),
		Q(KeyId(16u)),
		R(KeyId(17u)),
		S(KeyId(18u)),
		T(KeyId(19u)),
		U(KeyId(20u)),
		V(KeyId(21u)),
		W(KeyId(22u)),
		X(KeyId(23u)),
		Y(KeyId(24u)),
		Z(KeyId(25u)),
		Space(KeyId(40u)),
		Minus(KeyId(41u)),
		Equals(KeyId(42u)),
		LShift(KeyId(205u)),
	}

	sealed class KeyPredicate {
		// Automatically asserted helper
		internal class Helper(private val keys: Map<KeyId, Key>) {
			operator fun get(key: KeyId) = keys[key]!!
		}

		internal abstract fun test(keys: Helper): Boolean

		data class Down(val x: KeyId) : KeyPredicate() {
			override fun test(keys: Helper) = keys[x].down
		}

		data class JustDown(val x: KeyId) : KeyPredicate() {
			override fun test(keys: Helper) = keys[x].isJustDown()
		}

		data class JustUp(val x: KeyId) : KeyPredicate() {
			override fun test(keys: Helper) = keys[x].isJustUp()
		}

		data class And(val x: KeyPredicate, val y: KeyPredicate) : KeyPredicate() {
			override fun test(keys: Helper) = x.test(keys) && y.test(keys)
		}

		data class Or(val x: KeyPredicate, val y: KeyPredicate) : KeyPredicate() {
			override fun test(keys: Helper) = x.test(keys) || y.test(keys)
		}

		data class Not(val x: KeyPredicate) : KeyPredicate() {
			override fun test(keys: Helper) = !x.test(keys)
		}

		operator fun not() = Not(this)
		infix fun and(other: KeyPredicate) = And(this, other)
		infix fun or(other: KeyPredicate) = Or(this, other)
	}

	// This class may be programmatically generated
	object KeysScope {
		val A = KeyboardKeys.A
		val B = KeyboardKeys.B
		val C = KeyboardKeys.C
		val D = KeyboardKeys.D
		val E = KeyboardKeys.E
		val F = KeyboardKeys.F
		val G = KeyboardKeys.G
		val H = KeyboardKeys.H
		val I = KeyboardKeys.I
		val J = KeyboardKeys.J
		val K = KeyboardKeys.K
		val L = KeyboardKeys.L
		val M = KeyboardKeys.M
		val N = KeyboardKeys.N
		val O = KeyboardKeys.O
		val P = KeyboardKeys.P
		val Q = KeyboardKeys.Q
		val R = KeyboardKeys.R
		val S = KeyboardKeys.S
		val T = KeyboardKeys.T
		val U = KeyboardKeys.U
		val V = KeyboardKeys.V
		val W = KeyboardKeys.W
		val X = KeyboardKeys.X
		val Y = KeyboardKeys.Y
		val Z = KeyboardKeys.Z
		val Space = KeyboardKeys.Space
		val Minus = KeyboardKeys.Minus
		val Equals = KeyboardKeys.Equals
		val LShift = KeyboardKeys.LShift
	}

	init {
		// Refers to ferricia::mui::KeyboardKey
		for (i in 0u..242u) keys[KeyId(i)] = Key()
	}

	@JvmInline
	value class KeyId(private val id: UInt) {
		fun matches(other: Keys) = other.matches(this)
	}

	class Key {
		var down = false
			internal set
		internal var justChanged = false

		fun isJustDown() = down && justChanged
		fun isJustUp() = !down && justChanged
	}

	fun condition(predicate: KeysScope.() -> KeyPredicate) = KeysScope.predicate().test(KeyPredicate.Helper(keys))

	sealed class KeyEvent private constructor(internal open val key: KeyId) {
		data class Down(override val key: KeyId) : KeyEvent(key)
		data class Up(override val key: KeyId) : KeyEvent(key)
	}

	internal fun update(events: List<KeyEvent>) {
		keys.forEach { (_, key) -> key.justChanged = false }
		events.forEach {
			// Note: This may not handle the case where a key is just down less than a tick.
			keys[it.key]!!.justChanged = true
			when (it) {
				is KeyEvent.Down -> keys[it.key]!!.down = true
				is KeyEvent.Up -> keys[it.key]!!.down = false
			}
		}
	}
}
