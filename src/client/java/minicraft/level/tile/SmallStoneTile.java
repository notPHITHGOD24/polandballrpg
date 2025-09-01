package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteAnimation;
import minicraft.gfx.SpriteLinker;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.util.AdvancementElement;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Random;

public class SmallStoneTile extends Tile {
	private static final int NUM_VARIANTS = 2;
	private static final SpriteAnimation[] SPRITES = new SpriteAnimation[] {
		new SpriteAnimation(SpriteLinker.SpriteType.Tile, "small_rock_1")
			.setConnectionChecker((level, x, y, tile, side) -> tile instanceof SmallStoneTile),
		new SpriteAnimation(SpriteLinker.SpriteType.Tile, "small_rock_2")
			.setConnectionChecker((level, x, t, tile, side) -> tile instanceof SmallStoneTile)
	};

	protected SmallStoneTile() {
		super("Small Stone", null);
	}
	

	@Override
	public boolean connectsToGrass(Level level, int x, int y) {
		return true;
	}

	public static short getRandomData(Random random) {
		return (short) random.nextInt(NUM_VARIANTS);
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		Tiles.get("Grass").render(screen, level, x, y);
		int variant = level.getData(x, y); // Ensures that it is in range.
		SPRITES[variant].render(screen, level, x, y);
	}

	@Override
	public boolean tick(Level level, int xt, int yt) {
		//noinspection DuplicatedCode
		if (random.nextInt(30) != 0) return false;

		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tiles.get("Dirt")) {
			level.setTile(xn, yn, this);
			return true;
		}

		return false;
	}

	@Override
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Axe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					int data = level.getData(xt, yt);
					level.setTile(xt, yt, Tiles.get("Grass"));
					Sound.play("monsterhurt");
					if (random.nextInt(2) == 0) { // 50% chance to drop Small stones
						level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Stone"));
					}
					AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.INSTANCE.trigger(
						new AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.ItemUsedOnTileTriggerConditionHandler.ItemUsedOnTileTriggerConditions(
							item, this, data, xt, yt, level.depth));
					return true;
				}
			}
		}
		return false;
	}
}