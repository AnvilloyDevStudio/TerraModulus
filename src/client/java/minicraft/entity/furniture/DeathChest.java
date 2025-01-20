package minicraft.entity.furniture;

import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.core.io.Localization;
import minicraft.core.io.Settings;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteManager.SpriteLink;
import minicraft.gfx.SpriteManager.SpriteType;
import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.UnlimitedInventory;
import org.jetbrains.annotations.Nullable;

public class DeathChest extends Chest {
	private static SpriteLink normalSprite = new SpriteLink.SpriteLinkBuilder(SpriteType.Entity, "chest").createSpriteLink();
	private static SpriteLink redSprite = new SpriteLink.SpriteLinkBuilder(SpriteType.Entity, "red_chest").createSpriteLink();

	public int time; // Time passed (used for death chest despawn)
	private int redtick = 0; //This is used to determine the shade of red when the chest is about to expire.
	private boolean reverse; // What direction the red shade (redtick) is changing.

	/**
	 * Creates a custom chest with the name Death Chest
	 */
	public DeathChest() {
		super(new UnlimitedInventory(), "Death Chest", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "dungeon_chest").createSpriteLink());
		this.sprite = normalSprite;

		/// Set the expiration time based on the world difficulty.
		if (Settings.get("diff").equals("minicraft.settings.difficulty.easy")) {
			time = 450 * Updater.normSpeed;
		} else if (Settings.get("diff").equals("minicraft.settings.difficulty.normal")) {
			time = 300 * Updater.normSpeed;
		} else if (Settings.get("diff").equals("minicraft.settings.difficulty.hard")) {
			time = 150 * Updater.normSpeed;
		}
	}

	public DeathChest(Player player) {
		this();
		this.x = player.x;
		this.y = player.y;
		for (Item i : player.getInventory().getItemsView()) {
			inventory.add(i.copy());
		}
	}

	// For death chest time count, I imagine.
	@Override
	public void tick() {
		super.tick();
		//name = "Death Chest:"; // add the current

		if (inventory.invSize() == 0) {
			remove();
		}

		if (time < 30 * Updater.normSpeed) { // If there is less than 30 seconds left...
			redtick += reverse ? -1 : 1; // inc/dec-rement redtick, changing the red shading.

			/// These two statements keep the red color oscillating.
			if (redtick > 13) {
				reverse = true;
				this.sprite = normalSprite;
			}
			if (redtick < 0) {
				reverse = false;
				this.sprite = redSprite;
			}
		}

		if (time > 0) {
			time--; // Decrement the time if it is not already zero.
		}

		if (time == 0) {
			die(); // Remove the death chest when the time expires, spilling all the contents.
		}
	}

	public void render(Screen screen) {
		super.render(screen);
		String timeString = (time / Updater.normSpeed) + "S";
		Font.draw(timeString, screen, x - Font.textWidth(timeString) / 2, y - Font.textHeight() - getBounds().getHeight() / 2, Color.WHITE);
	}

	public boolean use(Player player, @Nullable Item item, Direction attackDir) {
		return false;
	} // can't open it, just walk into it.

	@Override
	public void touchedBy(Entity other) {
		if (other instanceof Player) {
			Inventory playerInv = ((Player) other).getInventory();
			for (Item i : inventory.getItemsView()) {
				if (playerInv.add(i) != null) {
					Game.inGameNotifications.add("Your inventory is full!");
					return;
				}

				inventory.removeItem(i);
			}

			remove();
			Game.inGameNotifications.add(Localization.getLocalized("minicraft.notification.death_chest_retrieved"));
		}
	}
}
