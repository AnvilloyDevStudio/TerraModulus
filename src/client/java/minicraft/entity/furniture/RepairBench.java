package minicraft.entity.furniture;

import minicraft.core.Game;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteManager;
import minicraft.item.Item;
import minicraft.screen.RepairBenchDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepairBench extends Furniture {
	private static final SpriteManager.SpriteLink sprite = new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteManager.SpriteType.Entity, "repair_bench")
		.createSpriteLink();
	private static final SpriteManager.SpriteLink itemSprite = new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteManager.SpriteType.Item, "repair_bench")
		.createSpriteLink();

	public RepairBench() {
		super("Repair Bench", sprite, itemSprite);
	}

	@Override
	public boolean use(Player player, @Nullable Item item, Direction attackDir) {
		Game.setDisplay(new RepairBenchDisplay(this, player));
		return true;
	}

	@Override
	public @NotNull Furniture copy() {
		return new RepairBench();
	}
}
