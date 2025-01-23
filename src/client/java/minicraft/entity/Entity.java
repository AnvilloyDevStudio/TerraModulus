package minicraft.entity;

import minicraft.core.Action;
import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.entity.mob.Player;
import minicraft.gfx.Rectangle;
import minicraft.gfx.Screen;
import minicraft.item.Item;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.network.Network;
import minicraft.util.DamageSource;
import minicraft.util.Logging;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;

public abstract class Entity implements Tickable {

	/* I guess I should explain something real quick. The coordinates between tiles and entities are different.
	 * The world coordinates for tiles is 128x128
	 * The world coordinates for entities is 2048x2048
	 * This is because each tile is 16x16 pixels big
	 * 128 x 16 = 2048.
	 * When ever you see a ">>", it means that it is a right shift operator. This means it shifts bits to the right (making them smaller)
	 * x >> 4 is the equivalent to x / (2^4). Which means it's dividing the X value by 16. (2x2x2x2 = 16)
	 * xt << 4 is the equivalent to xt * (2^4). Which means it's multiplying the X tile value by 16.
	 *
	 * These bit shift operators are used to easily get the X & Y coordinates of a tile that the entity is standing on.
	 */

	/*
	 * In total there are 3 types of active entity interactions: attack, use and take.
	 * While only players can use and take entities and tiles, generally all mobs may attack other entities and tiles.
	 * This is because players could use different keys for different interactions, but not for mob AIs.
	 * However, not all entities are able to be attacked (as well as use and take).
	 * Tiles can also attack entities in the similar sense.
	 * In the process of damage/hurting calculations and handling, the flow is like:
	 * - 1. Attack: interaction triggered by damage source; damage with bonus intended to emit is calculated here
	 *   - With a method if there exists an AI in the source
	 * - 2. Hurt: target entity receives damage from damage source; calculation includes armors and shields
	 * - 3. Handle damage: final damage value is passed to this when all damage calculations are completed
	 */

	// Entity coordinates are per pixel, not per tile; each tile is 16x16 entity pixels.
	protected final Random random = new Random();
	public int x, y; // x, y entity coordinates on the map
	private int xr, yr; // x, y radius of entity
	private boolean removed; // If the entity is to be removed from the level.
	protected Level level; // The level that the entity is on.
	public int col; // Current color.

	// Numeric unique identifier for the entity.
	public int eid;

	/**
	 * Default constructor for the Entity class.
	 * Assings null/none values to the instace variables.
	 * The exception is removed which is set to true, and
	 * lastUpdate which is set to System.nanoTime().
	 * @param xr X radius of entity.
	 * @param yr Y radius of entity.
	 */
	public Entity(int xr, int yr) { // Add color to this later, in color update
		this.xr = xr;
		this.yr = yr;

		level = null;
		removed = true;
		col = 0;

		eid = -1;
	}

	public abstract void render(Screen screen); // Used to render the entity on screen.

	@Override
	public abstract void tick(); // Used to update the entity.

	/**
	 * Returns true if the entity is removed from the level, otherwise false.
	 * @return removed
	 */
	public boolean isRemoved() {
		return removed/* || level == null*/;
	}

	/**
	 * Returns the level which this entity belongs in.
	 * @return level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Returns a Rectangle instance using the defined bounds of the entity.
	 */
	protected Rectangle getBounds() {
		return new Rectangle(x, y, xr * 2, yr * 2, Rectangle.CENTER_DIMS);
	}

	/**
	 * Returns true if this entity is found in the rectangle specified by given two coordinates.
	 */
	public boolean isTouching(Rectangle area) {
		return area.intersects(getBounds());
	}

	/**
	 * Returns if this entity stops other solid entities from moving.
	 */
	public boolean isSolid() {
		return true;
	} // Most entities are solid

	/**
	 * Determines if the given entity should prevent this entity from moving.
	 */
	public boolean blocks(Entity e) {
		return isSolid() && e.isSolid();
	}

	public boolean canSwim() {
		return false;
	} // Determines if the entity can swim (extended in sub-classes)

	public boolean canWool() {
		return false;
	} // This, strangely enough, determines if the entity can walk on wool; among some other things..?

	public boolean canBurn() {
		return true;
	} // Determines if the entity can burn.

	public boolean canBeAffectedByLava() {
		return true;
	} // Determines if the entity can burn in lava.

	public int burningDuration = 0;

	public int getLightRadius() {
		return 0;
	} // Used for lanterns... and player? that might be about it, though, so idk if I want to put it here.


	/**
	 * If this entity is touched by another entity (extended by sub-classes)
	 */
	protected void touchedBy(Entity entity) {
	}

	public boolean isFireImmune() {
		return false;
	}

	/**
	 * The amount of damage to attack when the item has no damage attribute set.
	 * @return amount of damage by fists
	 */
	protected int baseDamage() {
		return 1;
	}

	/**
	 * An indicator for which the entity is attackable by certain entity, even under certain conditions.
	 * This is invoked each time an entity is being targetted regardless it is interacted.
	 * Most probably used by Player.
	 * @return {@code false} if not attackable and {@link #hurt(DamageSource, Direction, int)} would not be invoked
	 * when triggered.
	 */
	public abstract boolean isAttackable(Entity source, @Nullable Item item, Direction attackDir);

	/**
	 * An indicator for which the entity is attackable by certain entity, even under certain conditions.
	 * This is invoked each time an entity is being targetted regardless it is interacted.
	 * Most probably used by Player.
	 * Though the usefulness of this is doubtable.
	 * @return {@code false} if not attackable and {@link #hurt(DamageSource, Direction, int)} would not be
	 * invoked when triggered.
	 */
	public abstract boolean isAttackable(Tile source, Level level, int x, int y, Direction attackDir);

	public boolean isInvulnerableTo(DamageSource source) {
		return isRemoved() ||
			source.getCausingEntity() instanceof Player && Game.isMode("minicraft.settings.mode.creative") ||
			source.getDamageType().isFireRelated() && isFireImmune();
	}

	/**
	 * An indicator for which the entity is attackable by certain entity, even under certain conditions.
	 * This is invoked each time an entity is being targetted regardless it is interacted.
	 * Most probably used by Player.
	 * @return {@code false} if not attackable and {@link #use(Player, Item, Direction)} would not be invoked
	 * when triggered.
	 */
	public abstract boolean isUsable();

	// TODO Here, attackDir may be changed to use an angle instead of axis to perform more accurate actions.

	/**
	 * Hurt the entity directly, based on only damage and a direction
	 * Usually this is invoked by {@link #hurt(DamageSource, Direction, int)}.<p>
	 * Note that using {@link #hurt(DamageSource, Direction, int)} is more recommended.
	 * @param damage The amount of damage to hurt the entity with
	 * @param attackDir The direction this entity was attacked from
	 */
	protected abstract void handleDamage(DamageSource source, Direction attackDir, int damage);

	/**
	 * Attacks the entity this method is called on
	 * @param source The cause of damage
	 * @param attackDir The direction to interact
	 * @param damage The amount of damage intended to emit this time
	 * @return If the interaction was successful
	 */
	public abstract boolean hurt(DamageSource source, Direction attackDir, int damage);

	public static Direction getInteractionDir(Entity attacker, Entity hurt) {
		return Direction.getDirection(hurt.x - attacker.x, hurt.y - attacker.y);
	}

	/**
	 * Called when the player presses the USE key in front of this.
	 */
	public boolean use(Player player, @Nullable Item item, Direction attackDir) {
		return false;
	}

	/**
	 * Picks up this entity
	 * @param player The player interacting
	 * @return the item picked up; {@code null} if picking up failed
	 */
	public @Nullable Item take(Player player) {
		return null;
	}

	/**
	 * Moves an entity horizontally and vertically. Returns whether entity was unimpeded in it's movement.
	 */
	public boolean move(int xd, int yd) {
		// TODO Validate existence of `Updater.saving` here, may potentially cause issue
		if ((xd == 0 && yd == 0)) return true; // Pretend that it kept moving

		boolean stopped = true; // Used to check if the entity has BEEN stopped, COMPLETELY; below checks for a lack of collision.
		// Either xd or yd must be non-zero, so at least either one of them is invoked.
		//noinspection RedundantIfStatement
		if (xd != 0 && moveX(xd)) stopped = false; // Becomes false if horizontal movement was successful.
		if (yd != 0 && moveY(yd)) stopped = false; // Becomes false if vertical movement was successful.
		if (!stopped) {
			int xt = x >> 4; // The x tile coordinate that the entity is standing on.
			int yt = y >> 4; // The y tile coordinate that the entity is standing on.
			level.getTile(xt, yt).steppedOn(level, xt, yt, this); // Calls the steppedOn() method in a tile's class. (used for tiles like sand (footprints) or lava (burning))
		}
		return !stopped;
	}

	/**
	 * Moves the entity a long only on X axis without "teleporting".
	 * Will throw exception otherwise.<br>
	 * Note that this should only be invoked by {@link #move(int, int)}.
	 * @param d Displacement relative to the axis; should be non-zero
	 * @return true if the move was successful, false if not.
	 */
	protected boolean moveX(int d) {
		//boolean interact = true;//!Game.isValidClient() || this instanceof ClientTickable;

		// Taking the axis of movement (towards) as the front axis, and the horizontal axis with another axis.
		// Signs taking front axis' directions, i.e. the x-axis.
		// Horizontal directions taking the number line directions, i.e. the y-axis.
		int sgn = (int) Math.signum(d); // -1 for LEFT; +1 for RIGHT
		int hitBoxLeft = y - yr;
		int hitBoxRight = y + yr;
		int hitBoxFront = x + xr * sgn;
		int maxFront = Level.calculateMaxFrontClosestTile(sgn, d, hitBoxLeft, hitBoxRight, hitBoxFront,
			(front, horTile) -> level.getTile(front, horTile).mayPass(level, front, horTile, this)); // Maximum position can be reached with front hit box
		if (maxFront == hitBoxFront) { // Bumping into the facing tile
			int hitBoxRightTile = hitBoxRight >> 4;
			int frontTile = (hitBoxFront + sgn) >> 4;
			for (int horTile = hitBoxLeft >> 4; horTile <= hitBoxRightTile; horTile++) {
				level.getTile(frontTile, horTile).bumpedInto(level, frontTile, horTile, this);
			}
			return false; // No movement can be made.
		}
		return moveByEntityHitBoxChecks(sgn, hitBoxFront, maxFront, () -> x + sgn, () -> y, () -> x += sgn, hitBoxLeft, hitBoxRight,
			(front, horTile) -> level.getTile(front, horTile).bumpedInto(level, front, horTile, this),
			(front, horTile) -> level.getTile(front, horTile).steppedOn(level, front, horTile, this));
	}

	/**
	 * Moves the entity a long only on X axis without "teleporting".
	 * Will throw exception otherwise.<br>
	 * Note that this should only be invoked by {@link #move(int, int)}.
	 * @param d Displacement relative to the axis; should be non-zero
	 * @return true if there is movement, false if not.
	 */
	protected boolean moveY(int d) {
		//boolean interact = true;//!Game.isValidClient() || this instanceof ClientTickable;

		// Taking the axis of movement (towards) as the front axis, and the horizontal axis with another axis.
		// Signs taking front axis' directions, i.e. the y-axis.
		// Horizontal directions taking the number line directions, i.e. the x-axis.
		int sgn = (int) Math.signum(d); // -1 for UP; +1 for DOWN
		int hitBoxLeft = x - xr;
		int hitBoxRight = x + xr;
		int hitBoxFront = y + yr * sgn;
		int maxFront = Level.calculateMaxFrontClosestTile(sgn, d, hitBoxLeft, hitBoxRight, hitBoxFront,
			(front, horTile) -> level.getTile(horTile, front).mayPass(level, horTile, front, this)); // Maximum position can be reached with front hit box
		if (maxFront == hitBoxFront) { // Bumping into the facing tile
			int hitBoxRightTile = hitBoxRight >> 4;
			int frontTile = (hitBoxFront + sgn) >> 4;
			for (int horTile = hitBoxLeft >> 4; horTile <= hitBoxRightTile; horTile++) {
				level.getTile(horTile, frontTile).bumpedInto(level, horTile, frontTile, this);
			}
			return false; // No movement can be made.
		}
		return moveByEntityHitBoxChecks(sgn, hitBoxFront, maxFront, () -> x, () -> y + sgn, () -> y += sgn, hitBoxLeft, hitBoxRight,
			(front, horTile) -> level.getTile(horTile, front).bumpedInto(level, horTile, front, this),
			(front, horTile) -> level.getTile(horTile, front).steppedOn(level, horTile, front, this));
	}

	/**
	 * Moves the entity by checking entity hit boxes being interacted with the given possible length of straight path.
	 * @param sgn One-dimensional direction of displacement
	 * @param hitBoxFront The front boundary of hit box
	 * @param maxFront Maximum position can be reached with front hit box (firstly checked by tile hot box)
	 * @param xMove The value of the willing x movement
	 * @param yMove The value of the willing y movement
	 * @param incrementMove The movement call when the movement is possible
	 * @param hitBoxLeft The left boundary of hit box
	 * @param hitBoxRight The right boundary of hit box
	 * @param bumpingHandler The consumer handling bumping into a new tile;
	 * 	the first parameter takes the front tile position and second one takes the horizontal position
	 * @param steppingHandler The consumer handling stepping on a new tile;
	 * 	the first parameter takes the front tile position and second one takes the horizontal position
	 * @return {@code true} if the movement is successful, {@code false} otherwise.
	 * @see Level#calculateMaxFrontClosestTile(int, int, int, int, int, BiPredicate)
	 */
	protected boolean moveByEntityHitBoxChecks(int sgn, int hitBoxFront, int maxFront, IntSupplier xMove,
	                                           IntSupplier yMove, Action incrementMove, int hitBoxLeft, int hitBoxRight,
	                                           BiConsumer<Integer, Integer> bumpingHandler, BiConsumer<Integer, Integer> steppingHandler) {
		boolean successful = false;

		// These lists are named as if the entity has already moved-- it hasn't, though.
		HashSet<Entity> wasInside = new HashSet<>(level.getEntitiesInRect(getBounds())); // Gets all the entities that are inside this entity (aka: colliding) before moving.
		int frontTile = hitBoxFront << 4; // The original tile the front boundary hit box staying on
		boolean handleSteppedOn = false; // Used together with frontTile
		for (int front = hitBoxFront; sgn < 0 ? front > maxFront : front < maxFront; front += sgn) {
			int newFrontTile = (front + sgn) >> 4;
			if (newFrontTile != frontTile) { // New tile touched
				int hitBoxRightTile = hitBoxRight >> 4;
				for (int horTile = hitBoxLeft >> 4; horTile <= hitBoxRightTile; horTile++) {
					bumpingHandler.accept(newFrontTile, horTile);
				}
				frontTile = newFrontTile;
				handleSteppedOn = true;
			}
			boolean blocked = false; // If the entity prevents this one from movement, no movement.
			for (Entity e : level.getEntitiesInRect(new Rectangle(xMove.getAsInt(), yMove.getAsInt(), xr * 2, yr * 2, Rectangle.CENTER_DIMS))) {
				if (!wasInside.contains(e)) { // Skips entities that were touched.
					if (e != this) {
						if (!blocked) blocked = e.blocks(this);
						if (e instanceof Player) {
							touchedBy(e);
						} else {
							e.touchedBy(this);
						}
					}
					wasInside.add(e); // Add entity into list.
				}
			}
			if (blocked) break;
			incrementMove.act(); // Movement successful
			if (handleSteppedOn) { // When the movement to a new tile successes
				int hitBoxRightTile = hitBoxRight >> 4;
				for (int horTile = hitBoxLeft >> 4; horTile <= hitBoxRightTile; horTile++) {
					steppingHandler.accept(frontTile, horTile); // Calls the steppedOn() method in a tile's class. (used for tiles like sand (footprints) or lava (burning))
				}
			}
			successful = true;
		}

		return successful;
	}

	/**
	 * Checks if the entity is able to naturally be despawned in general conditions. Handles (despawns) if true.
	 */
	public void handleDespawn() {
	}

	/**
	 * This exists as a way to signify that the entity has been removed through player action and/or world action; basically, it's actually gone, not just removed from a level because it's out of range or something. Calls to this method are used to, say, drop items.
	 */
	public void die() { // TODO damage type
		remove();
	}

	/**
	 * Removes the entity from the level.
	 */
	public void remove() {
		if (removed && !(this instanceof ItemEntity)) // Apparently this happens fairly often with item entities.
			Logging.ENTITY.debug("Note: remove() called on removed entity: {}", this);

		removed = true;

		if (level == null)
			Logging.ENTITY.debug("Note: remove() called on entity with no level reference: {}", getClass());
		else
			level.remove(this);
	}

	/**
	 * This should ONLY be called by the Level class. To properly remove an entity from a level, use level.remove(entity)
	 */
	public void remove(Level level) {
		if (level != this.level) {
			Logging.ENTITY.warn("Tried to remove entity {} from level it is not in: {}; in level {}",
				this, level, this.level);
		} else {
			removed = true; // Should already be set.
			this.level = null;
		}
	}

	/**
	 * This should ONLY be called by the Level class. To properly add an entity to a level, use level.add(entity)
	 */
	public void setLevel(Level level, int x, int y) {
		if (level == null) {
			Logging.ENTITY.warn("Tried to set level of entity {} to a null level; Should use remove(level)", this);
			return;
		}

		this.level = level;
		removed = false;
		this.x = x;
		this.y = y;

		if (eid < 0)
			eid = Network.generateUniqueEntityId();
	}

	public boolean isWithin(int tileRadius, Entity other) {
		if (level == null || other.getLevel() == null) return false;
		if (level.depth != other.getLevel().depth)
			return false; // Obviously, if they are on different levels, they can't be next to each other.

		double distance = Math.abs(Math.hypot(x - other.x, y - other.y)); // Calculate the distance between the two entities, in entity coordinates.

		return Math.round(distance) >> 4 <= tileRadius; // Compare the distance (converted to tile units) with the specified radius.
	}

	/**
	 * Returns the closest player to this entity.
	 * @return the closest player.
	 */
	@Nullable
	protected Player getClosestPlayer() {
		return getClosestPlayer(true);
	}

	/**
	 * Returns the closes player to this entity.
	 * If this is called on a player it can return itself.
	 * @param returnSelf determines if the method can return itself.
	 * @return The closest player to this entity.
	 */
	@Nullable
	protected Player getClosestPlayer(boolean returnSelf) {
		if (this instanceof Player && returnSelf)
			return (Player) this;

		if (level == null) return null;

		return level.getClosestPlayer(x, y);
	}

	public String toString() {
		return getClass().getSimpleName() + getDataPrints();
	}

	protected List<String> getDataPrints() {
		List<String> prints = new ArrayList<>();
		prints.add("eid=" + eid);
		return prints;
	}

	@Override
	public final boolean equals(Object other) {
		return other instanceof Entity && hashCode() == other.hashCode();
	}

	@Override
	public final int hashCode() {
		return eid;
	}
}
