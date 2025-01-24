package minicraft.entity.mob;

import minicraft.core.io.Settings;
import minicraft.gfx.SpriteManager.SpriteLink;
import minicraft.item.Items;

public class Zombie extends EnemyMob {
	private static SpriteLink[][][] sprites = new SpriteLink[][][] {
		Mob.compileMobSpriteAnimations(0, 0, "zombie"),
		Mob.compileMobSpriteAnimations(0, 2, "zombie"),
		Mob.compileMobSpriteAnimations(0, 4, "zombie"),
		Mob.compileMobSpriteAnimations(0, 6, "zombie")
	};

	/**
	 * Creates a zombie of the given level.
	 * @param lvl Zombie's level.
	 */
	public Zombie(int lvl) {
		super(lvl, sprites, 5, 100);
	}

	public void die() {
// 		if (Settings.get("diff").equals("minicraft.displays.world_create.options.difficulty.easy")) dropItem(2, 4, Items.get("cloth"));
// 		if (Settings.get("diff").equals("minicraft.displays.world_create.options.difficulty.normal")) dropItem(1, 3, Items.get("cloth"));
// 		if (Settings.get("diff").equals("minicraft.displays.world_create.options.difficulty.hard")) dropItem(1, 2, Items.get("cloth"));

		if (random.nextInt(60) == 2) {
			level.dropItem(x, y, Items.get("iron"));
		}

		if (random.nextInt(40) == 19) {
			int rand = random.nextInt(3);
			if (rand == 0) {
				level.dropItem(x, y, Items.get("green clothes"));
			} else if (rand == 1) {
				level.dropItem(x, y, Items.get("red clothes"));
			} else if (rand == 2) {
				level.dropItem(x, y, Items.get("blue clothes"));
			}
		}

		if (random.nextInt(100) < 4) {
			level.dropItem(x, y, Items.get("Potato"));
		}

		super.die();
	}
}
