package minicraft.item;

import java.util.ArrayList;

import minicraft.core.Game;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Point;
import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.gfx.SpriteLinker.SpriteType;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import sun.print.BackgroundLookupListener;

import static minicraft.core.Game.player;
import static minicraft.entity.mob.Player.INTERACT_DIST;

public class BottleItem extends StackableItem {

	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();

		items.add(new BottleItem("Drinking Bottle", new LinkedSprite(SpriteType.Item, "glass_bottle")));

		return items;
	}

	private BottleItem(String name, LinkedSprite sprite) {
		this(name, sprite, 1);
	}

	private BottleItem(String name, LinkedSprite sprite, int count) {
		super(name, sprite, count);
	}

	
	public boolean displayBox() {
		return true;
	}
	
	private Point getInteractionTile() {
		int x = player.x, y = player.y - 2;

		x += player.dir.getX()*INTERACT_DIST;
		y += player.dir.getY()*INTERACT_DIST;

		return new Point(x >> 4, y >> 4);
	}
	/** What happens when the player uses the item on a tile */
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		boolean success = false;
		//interaction code
		Point t = getInteractionTile();
		if(level.getTile(t.x,t.y).id==6) {
			player.tryAddToInvOrDrop(Items.get("Dirty water"));
			success = true;
		}


		return super.interactOn(success);
	}

	@Override
	public boolean interactsWithWorld() {
		return true;
	}

	public BottleItem copy() {
		return new BottleItem(getName(), sprite, count);
	}
}
