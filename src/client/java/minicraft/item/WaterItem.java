package minicraft.item;

import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.gfx.SpriteLinker.SpriteType;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WaterItem extends StackableItem {

	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();

		items.add(new WaterItem("Glass Water", new LinkedSprite(SpriteType.Item, "water_glass"), 1));

		return items;
	}

	private final int feed; // The amount of hunger the food "satisfies" you by.
	private static final int staminaCost = 2; // The amount of stamina it costs to consume the food.

	private WaterItem(String name, LinkedSprite sprite, int feed) {
		this(name, sprite, 1, feed);
	}

	private WaterItem(String name, LinkedSprite sprite, int count, int feed) {
		super(name, sprite, count);
		this.feed = feed;
	}

	/**
	 * What happens when the player uses the item on a tile
	 */
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		boolean success = false;
		if (count > 0 && player.water < Player.maxWater && player.payStamina(staminaCost)) { // If the player has hunger to fill, and stamina to pay...
			player.water = Math.min(player.water + feed, Player.maxWater); // Restore the hunger
			success = true;
		}

		return super.interactOn(success);
	}

	@Override
	public boolean interactsWithWorld() {
		return false;
	}

	public @NotNull WaterItem copy() {
		return new WaterItem(getName(), sprite, count, feed);
	}
}
