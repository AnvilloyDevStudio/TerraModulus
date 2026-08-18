/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import net.terramodulus.mui.gui.asd.AsdHandle
import kotlin.collections.mutableSetOf

internal class LayoutManager(screenManager: ScreenManager) {
	private val agimoTree = AgimoTree(screenManager)

	// TODO maybe later think of a way to dynamically update nodes partially
	private class AgimoTree(screenManager: ScreenManager) : AgimoTreeVisitor() {
		private val screenVisitor = screenManager.visitScreens()
		private val menuVisitor = screenManager.visitMenus()
		var root = RootNode(sequenceOf(), sequenceOf())

		private fun update() {
			root = RootNode(screenVisitor.visit(), menuVisitor.visit())
		}

		private fun listContainers(): Map<ContainerNode, AsdHandle> {
			val containers = mutableMapOf<ContainerNode, AsdHandle>()
			fun pushElements(node: ContainerNode) {
				node.elements.forEach {
					if (it is PaneNode) {
						containers[it] = it.component.asdHandle
						pushElements(it)
					}
				}
			}
			fun pushMenu(node: MenuNode) {
				containers[node] = node.menu.asdHandle
				pushElements(node)
			}
			fun pushScreen(node: ScreenNode) {
				containers[node] = node.screen.asdHandle
				node.menus.list.forEach(::pushMenu)
				pushElements(node)
			}
			root.screens.list.forEach(::pushScreen)
			root.menus.list.forEach(::pushMenu)
			return containers
		}

		fun discoverLayoutChanges(): LayoutChanges {
			val old = listContainers()
			update()
			val new = listContainers()
			return LayoutChanges(
				old.filter { !new.containsKey(it.key) }.entries.associate { it.value to it.key.layout },
				new.filter { !old.containsKey(it.key) }.entries.associate { it.value to it.key.layout },
				new.filter { it.key.layout.updated }.entries.associate { it.value to it.key.layout },
			)
		}

		class LayoutChanges(
			val removed: Map<AsdHandle, Layout>,
			val added: Map<AsdHandle, Layout>,
			val updated: Map<AsdHandle, Layout>,
		)
	}

	private data class LayoutNode(
		val layout: Layout,
		val containerHandle: AsdHandle,
		var group: LayoutComputationGroup,
		var units: Set<LayoutComputationUnit>,
	)

	private typealias InstancedPropertyMap<V> = MutableMap<AsdHandle, MutableMap<AgimoPropertyMap.Key<*>, V>>

	private operator fun <V> InstancedPropertyMap<V>.get(handle: AsdHandle, key: AgimoPropertyMap.Key<*>) =
		this[handle]?.get(key)

	private inline fun <V : MutableCollection<E>, E> InstancedPropertyMap<V>
		.push(handle: AsdHandle, key: AgimoPropertyMap.Key<*>, value: E, crossinline ifAbsent: () -> V) =
		computeIfAbsent(handle) { mutableMapOf() }.computeIfAbsent(key) { ifAbsent() }.add(value)

// 	private fun <V : MutableSet<E>, E> InstancedPropertyMap<V>
// 		.push(handle: AsdHandle, key: AgimoPropertyMap.Key<*>, value: E) =
// 		push(handle, key, value) { mutableSetOf<E>() as MutableCollection<E> }

	private inner class LayoutStates {
		private val layouts = mutableMapOf<AsdHandle, LayoutNode>()
		private val groupDeps: InstancedPropertyMap<MutableSet<LayoutComputationGroup>> = mutableMapOf()
		private val unitDeps: InstancedPropertyMap<MutableSet<LayoutComputationUnit>> = mutableMapOf()
		private val groups = mutableMapOf<LayoutComputationGroup, Set<LayoutComputationUnit>>()
		private val results = mutableMapOf<LayoutComputationUnit, Map<AsdHandle, AgimoPropertyMap>>()
		private val affectedProps = mutableMapOf<AsdHandle, MutableSet<AgimoPropertyMap.Key<*>>>()
		private val changedUnits = mutableSetOf<LayoutComputationUnit>()

		fun getLayout(asdHandle: AsdHandle) = layouts[asdHandle]

		fun addLayout(asdHandle: AsdHandle, layout: LayoutNode) {
			if (layouts.containsKey(asdHandle)) throw IllegalStateException()
			layouts[asdHandle] = layout
		}

		private fun addAffectedProp(handle: AsdHandle, key: AgimoPropertyMap.Key<*>) {
			affectedProps.computeIfAbsent(handle) { mutableSetOf() }.add(key)
		}

		fun validateCyclicGraphs() {
			run { // Groups
				data class GroupNode(
					val deps: Set<Pair<AsdHandle, AgimoPropertyMap.Key<*>>>,
					val children: Set<Pair<AsdHandle, AgimoPropertyMap.Key<*>>>,
				)
				data class PropNode(
					val deps: Set<GroupNode>,
					val children: Set<GroupNode>,
				)
				val visited = mutableSetOf<GroupNode>()
				val visiting = mutableSetOf<GroupNode>()
				layouts.values.map {
					GroupNode(
						it.group.dependencies.flatMap { (handle, keys) -> keys.map { it -> handle to it } }.toSet(),
						it.units.flatMap { it ->
							it.results.flatMap { (handle, keys) -> keys.map { it -> handle to it } }
						}.toSet(),
					)
				}

			}
			run { // Units

			}
		}

		fun removeLayout(asdHandle: AsdHandle) {
			val layout = layouts.remove(asdHandle)
			if (layout === null) throw IllegalStateException()
			groups.remove(layout.group)
			layout.group.dependencies.forEach { (handle, keys) ->
				keys.forEach { groupDeps[handle, it]?.remove(layout.group) }
			}
			layout.units.forEach { unit ->
				unit.dependencies.forEach { (handle, keys) ->
					keys.forEach { unitDeps[handle, it]?.remove(unit) }
				}
				changedUnits.add(unit)
			}
		}
	}

	internal fun tick() {
		val changes = agimoTree.discoverLayoutChanges()

	}
}
