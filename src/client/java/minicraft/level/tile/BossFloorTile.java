package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.ObsidianKnight;
import minicraft.entity.mob.Player;
import minicraft.item.Item;
import minicraft.item.ToolItem;
import minicraft.level.Level;
import minicraft.util.DisplayString;
import org.jetbrains.annotations.Nullable;

public class BossFloorTile extends FloorTile {
	private static final DisplayString floorMsg = Localization.getStaticDisplay(
		"minicraft.notification.defeat_obsidian_knight_first");

	protected BossFloorTile() {
		super(Material.Obsidian, "Boss Floor");
	}

	@Override
	public boolean hurt(Level level, int x, int y, Entity source, @Nullable Item item, Direction attackDir, int damage) {
		if ((!ObsidianKnight.beaten || ObsidianKnight.active) && !Game.isMode("minicraft.displays.world_create.options.game_mode.creative") && source instanceof Player) {
			if (item instanceof ToolItem) {
				ToolItem tool = (ToolItem) item;
				if (tool.type == type.getRequiredTool()) {
					if (((Player) source).payStamina(1)) {
						Game.inGameNotifications.add(floorMsg);
						Sound.play("monsterhurt");
						return true;
					}
				}
			}

			return false;
		}

		return super.hurt(level, x, y, source, item, attackDir, damage);
	}
}
