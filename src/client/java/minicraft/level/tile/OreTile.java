package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteAnimation;
import minicraft.gfx.SpriteManager.SpriteType;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.screen.AchievementsDisplay;
import minicraft.util.AdvancementElement;
import org.jetbrains.annotations.Nullable;

/// this is all the spikey stuff (except "cloud cactus")
public class OreTile extends Tile {
	private final OreType type;

	public enum OreType {
		Iron(Items.get("Iron Ore"), new SpriteAnimation(SpriteType.Tile, "iron_ore")),
		Lapis(Items.get("Lapis"), new SpriteAnimation(SpriteType.Tile, "lapis_ore")),
		Gold(Items.get("Gold Ore"), new SpriteAnimation(SpriteType.Tile, "gold_ore")),
		Gem(Items.get("Gem"), new SpriteAnimation(SpriteType.Tile, "gem_ore")),
		Cloud(Items.get("Cloud Ore"), new SpriteAnimation(SpriteType.Tile, "cloud_ore"));

		private final Item drop;
		public final SpriteAnimation sheet;

		OreType(Item drop, SpriteAnimation sheet) {
			this.drop = drop;
			this.sheet = sheet;
		}

		private Item getOre() {
			return drop.copy();
		}
	}

	protected OreTile(OreType o) {
		super((o == OreTile.OreType.Lapis ? "Lapis" : o == OreType.Cloud ? "Cloud Cactus" : o.name() + " Ore"), o.sheet);
		this.type = o;
	}

	public void render(Screen screen, Level level, int x, int y) {
		if (type == OreType.Cloud)
			Tiles.get("cloud").render(screen, level, x, y);
		else
			Tiles.get("dirt").render(screen, level, x, y);
		sprite.render(screen, level, x, y);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	@Override
	public boolean hurt(Level level, int x, int y, Entity source, @Nullable Item item, Direction attackDir, int damage) {
		if (Game.isMode("minicraft.settings.mode.creative")) {
			handleDamage(level, x, y, source, item, 100);
			return true;
		}

		if (item instanceof ToolItem && source instanceof Player) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (((Player) source).payStamina(6 - tool.level) && tool.payDurability()) {
					int data = level.getData(x, y);
					handleDamage(level, x, y, source, tool, tool.getDamage());
					AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.INSTANCE.trigger(
						new AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.ItemUsedOnTileTriggerConditionHandler.ItemUsedOnTileTriggerConditions(
							item, this, data, x, y, level.depth));
					return true;
				}
			}
		}

		handleDamage(level, x, y, source, item, 0);
		return true;
	}

	public Item getOre() {
		return type.getOre();
	}

	@Override
	protected void handleDamage(Level level, int x, int y, Entity source, @Nullable Item item, int dmg) {
		int damage = level.getData(x, y) + dmg;
		int oreH = random.nextInt(10) * 4 + 20;
		if (Game.isMode("minicraft.settings.mode.creative")) dmg = damage = oreH;

		level.add(new SmashParticle(x << 4, y << 4));
		Sound.play("monsterhurt");

		level.add(new TextParticle("" + dmg, (x << 4) + 8, (y << 4) + 8, Color.RED));
		if (dmg > 0) {
			int count = random.nextInt(2);
			if (damage >= oreH) {
				if (type == OreType.Cloud) {
					level.setTile(x, y, Tiles.get("Cloud"));
				} else {
					level.setTile(x, y, Tiles.get("Dirt"));
				}
				count += 2;
			} else {
				level.setData(x, y, damage);
			}
			if (type.drop.equals(Items.get("gem"))) {
				AchievementsDisplay.setAchievement("minicraft.achievement.find_gem", true);
			}
			level.dropItem((x << 4) + 8, (y << 4) + 8, count, type.getOre());
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		/// this was used at one point to hurt the player if they touched the ore; that's probably why the sprite is so spikey-looking.
	}
}
