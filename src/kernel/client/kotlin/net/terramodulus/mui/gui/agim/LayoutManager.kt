/*
 * SPDX-FileCopyrightText: 2026 TerraModulus Team and Contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.terramodulus.mui.gui.agim

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.terramodulus.mui.gui.asd.AsdHandle
import kotlin.collections.component1
import kotlin.collections.component2
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
		val groups: Sequence<LayoutComputationGroup>,
// 		var units: Set<LayoutComputationUnit>,
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
		private val unitResults: InstancedPropertyMap<MutableSet<LayoutComputationUnit>> = mutableMapOf()
		private val units = mutableMapOf<LayoutComputationGroup, Set<LayoutComputationUnit>>()
		private val unitGroups = mutableMapOf<LayoutComputationUnit, LayoutComputationGroup>()
		private val results = mutableMapOf<LayoutComputationUnit, Map<AsdHandle, AgimoPropertyMap>>()
		private val affectedProps = mutableMapOf<AsdHandle, MutableSet<AgimoPropertyMap.Key<*>>>()
// 		private val changedUnits = mutableSetOf<LayoutComputationUnit>()

		fun getLayout(asdHandle: AsdHandle) = layouts[asdHandle]

		fun addLayout(asdHandle: AsdHandle, layout: LayoutNode) {
			if (layouts.containsKey(asdHandle)) throw IllegalStateException()
			layouts[asdHandle] = layout
			layout.groups.forEach { group ->
				group.dependencies.forEach { (handle, keys) ->
					keys.forEach {
						groupDeps.push(handle, it, group) { mutableSetOf() }
					}
				}
			}
		}

		private fun addAffectedProp(handle: AsdHandle, key: AgimoPropertyMap.Key<*>) {
			affectedProps.computeIfAbsent(handle) { mutableSetOf() }.add(key)
		}

		private fun compute() {
			// TODO Currently groups have no dependencies, so not sure how this should be handled
			// However, instancing Groups does not depend on states of Layouts,
			// but computations of Units from Groups depend on states of Layouts.

			val unitsToCompute = mutableSetOf<LayoutComputationUnit>()
			// Recompute for Units where needed
			run {
				val unitsToRecompute = mutableSetOf<LayoutComputationUnit>()
				affectedProps.forEach { (handle, keys) ->
					keys.forEach {
						unitDeps[handle, it]?.apply { unitsToRecompute.addAll(this) }
					}
				}
				affectedProps.clear()
				val groupsToCompute = mutableSetOf<LayoutComputationGroup>()
				unitsToRecompute.forEach {
					val group = unitGroups[it]!!
					if (groupsToCompute.add(group)) removeGroup(group)
				}
				layouts.values.forEach { layout ->
					layout.groups.forEach {
						if (!units.containsKey(it)) groupsToCompute.add(it)
					}
				}
				groupsToCompute.forEach { group ->
					// Ignore dependencies and LayoutHandle used for Group.conditions at the moment
					group.conditions().apply {
						units[group] = this
					}.forEach { unit ->
						unitsToCompute.add(unit)
						unitGroups[unit] = group
						unit.dependencies.forEach { (handle, keys) ->
							keys.forEach {
								unitDeps.push(handle, it, unit) { mutableSetOf() }
							}
						}
						unit.results.forEach { (handle, keys) ->
							keys.forEach {
								unitResults.push(handle, it, unit) { mutableSetOf() }
							}
						}
					}
				}
			}

			// Compute Units while respecting their dependencies
			units.values.asSequence().flatten().forEach { if (!results.containsKey(it)) unitsToCompute.add(it) }
// 			val parents = mutableMapOf<LayoutComputationUnit, LayoutComputationUnit>()
// 			val children = mutableMapOf<LayoutComputationUnit, MutableSet<LayoutComputationUnit>>()
// 			val roots = mutableSetOf<LayoutComputationUnit>()
			// one coroutine put Units to the forest and send ?? (do we really need this?)
			// one coroutine checks for computability of Units (by dependencies)
			//   - compute result dependencies that are required by awaiting Units at the moment,
			//     only when those result dependencies are with uncomputed Units
			//   - Note: if any case some Units are dependencies but computed already, already fulfilled
			//   - send fulfilled computable Units to another coroutine
			// one coroutine receives computable Units then compute Units sequentially in its scope
			// with dependencies computed and combined as Handle,
			// then notify another coroutine for dependencies by computed results
			runBlocking {
				val channelToCompute = Channel<LayoutComputationUnit>(UNLIMITED)
				val channelComputed = Channel<LayoutComputationUnit>(UNLIMITED)
				launch {
					val unitResults: InstancedPropertyMap<MutableSet<LayoutComputationUnit>> = mutableMapOf()
					unitsToCompute.forEach { unit ->
						unit.results.forEach { (handle, keys) ->
							keys.forEach {
								unitResults.push(handle, it, unit) { mutableSetOf() }
							}
						}
					}
					val unitDeps = mutableMapOf<LayoutComputationUnit, MutableSet<LayoutComputationUnit>>()
					val unitChildren = mutableMapOf<LayoutComputationUnit, MutableSet<LayoutComputationUnit>>()
					unitsToCompute.forEach { unit ->
						unit.dependencies.forEach { (handle, keys) ->
							keys.forEach { key ->
								val deps = unitResults[handle, key]
								if (deps !== null) {
									deps.forEach {
										unitDeps.computeIfAbsent(unit) { mutableSetOf() }.add(it)
										unitChildren.computeIfAbsent(it) { mutableSetOf() }.add(unit)
									}
								}
							}
						}
					}
					// First compute Units without any dependency needed to be computed
					unitsToCompute.iterator().apply {
						while (hasNext()) {
							val unit = next()
							if (unitDeps[unit] === null) {
								channelToCompute.send(unit)
								remove()
							}
						}
					}
					for (unit in channelComputed) {
						unitChildren.remove(unit)?.forEach {
							val deps = requireNotNull(unitDeps[it])
							assert(deps.remove(unit))
							if (deps.isEmpty()) {
								channelToCompute.send(it)
								unitDeps.remove(it)
								unitsToCompute.remove(it)
							}
						}
						if (unitsToCompute.isEmpty()) {
							channelToCompute.close()
							assert(unitChildren.isEmpty())
							assert(unitDeps.isEmpty())
						}
					}
				}
				launch {
					for (unit in channelToCompute) {

					}
					channelComputed.close()
				}
			}
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
// 					GroupNode(
// 						it.group.dependencies.flatMap { (handle, keys) -> keys.map { it -> handle to it } }.toSet(),
// 						it.units.flatMap { it ->
// 							it.results.flatMap { (handle, keys) -> keys.map { it -> handle to it } }
// 						}.toSet(),
// 					)
				}

			}
			run { // Units

			}
		}

		fun removeLayout(asdHandle: AsdHandle) {
			val layout = layouts.remove(asdHandle)
			if (layout === null) throw IllegalStateException()
			layout.groups.forEach { removeGroup(it) }
		}

		private fun removeGroup(group: LayoutComputationGroup) {
			group.dependencies.forEach { (handle, keys) ->
				keys.forEach { groupDeps[handle, it]?.remove(group) }
			}
			units.remove(group)?.forEach { unit ->
				results.remove(unit)
				unitGroups.remove(unit)
				unit.dependencies.forEach { (handle, keys) ->
					keys.forEach { unitDeps[handle, it]?.remove(unit) }
				}
			}
		}
	}

	internal fun tick() {
		val changes = agimoTree.discoverLayoutChanges()

	}
}
