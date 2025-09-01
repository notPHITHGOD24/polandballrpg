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
import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.gfx.SpriteLinker.SpriteType;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.screen.AchievementsDisplay;
import minicraft.util.AdvancementElement;

public class PalmTile extends Tile {
	private static final LinkedSprite oakSprite = new LinkedSprite(SpriteType.Tile, "palm");
	private static final LinkedSprite oakSpriteFull = new LinkedSprite(SpriteType.Tile, "palm_full");

	public enum PalmType {
		OAK(oakSprite, oakSpriteFull);
		
		private final LinkedSprite palmSprite;
		private final LinkedSprite palmSpriteFull;

		PalmType(LinkedSprite palmSprite, LinkedSprite palmSpriteFull) {
			this.palmSprite = palmSprite;
			this.palmSpriteFull = palmSpriteFull;
		}
	}

	protected PalmTile(String name) {
		super(name, null);
	}

	@Override
	public boolean connectsToSand(Level level, int x, int y) {
		return true;
	}

	@SuppressWarnings("PointlessArithmeticExpression")
	public void render(Screen screen, Level level, int x, int y) {
		Tiles.get("Sand").render(screen, level, x, y);

		PalmType thisType = level.palmTypes[x + y * level.w];
		// Checking whether the target direction has targeted the same PalmTile
		boolean isUpTileSame = level.getTile(x, y - 1) == this && thisType == level.palmTypes[x + (y - 1) * level.w];
		boolean isLeftTileSame = level.getTile(x - 1, y) == this && thisType == level.palmTypes[(x - 1) + y * level.w];
		boolean isRightTileSame = level.getTile(x + 1, y) == this && thisType == level.palmTypes[(x + 1) + y * level.w];
		boolean isDownTileSame = level.getTile(x, y + 1) == this && thisType == level.palmTypes[x + (y + 1) * level.w];
		boolean isUpLeftTileSame = level.getTile(x - 1, y - 1) == this && thisType == level.palmTypes[(x - 1) + (y - 1) * level.w];
		boolean isUpRightTileSame = level.getTile(x + 1, y - 1) == this && thisType == level.palmTypes[(x + 1) + (y - 1) * level.w];
		boolean isDownLeftTileSame = level.getTile(x - 1, y + 1) == this && thisType == level.palmTypes[(x - 1) + (y + 1) * level.w];
		boolean isDownRightTileSame = level.getTile(x + 1, y + 1) == this && thisType == level.palmTypes[(x + 1) + (y + 1) * level.w];

		Sprite sprite = level.palmTypes[x + y * level.w].palmSprite.getSprite();
		Sprite spriteFull = level.palmTypes[x + y * level.w].palmSpriteFull.getSprite();

		if (isUpTileSame && isUpLeftTileSame && isLeftTileSame) {
			screen.render((x << 4) + 0, (y << 4) + 0, spriteFull.spritePixels[0][1]);
		} else {
			screen.render((x << 4) + 0, (y << 4) + 0, sprite.spritePixels[0][0]);
		}

		if (isUpTileSame && isUpRightTileSame && isRightTileSame) {
			screen.render((x << 4) + 8, (y << 4) + 0, spriteFull.spritePixels[0][0]);
		} else {
			screen.render((x << 4) + 8, (y << 4) + 0, sprite.spritePixels[0][1]);
		}

		if (isDownTileSame && isDownLeftTileSame && isLeftTileSame) {
			screen.render((x << 4) + 0, (y << 4) + 8, spriteFull.spritePixels[1][1]);
		} else {
			screen.render((x << 4) + 0, (y << 4) + 8, sprite.spritePixels[1][0]);
		}

		if (isDownTileSame && isDownRightTileSame && isRightTileSame) {
			screen.render((x << 4) + 8, (y << 4) + 8, spriteFull.spritePixels[1][0]);
		} else {
			screen.render((x << 4) + 8, (y << 4) + 8, sprite.spritePixels[1][1]);
		}
	}

	public boolean tick(Level level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) {
			level.setData(xt, yt, damage - 1);
			return true;
		}
		return false;
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	@Override
	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		hurt(level, x, y, dmg);
		return true;
	}

	@Override
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (Game.isMode("minicraft.settings.mode.creative"))
			return false; // Go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Axe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					int data = level.getData(xt, yt);
					hurt(level, xt, yt, tool.getDamage());
					AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.INSTANCE.trigger(
						new AdvancementElement.AdvancementTrigger.ItemUsedOnTileTrigger.ItemUsedOnTileTriggerConditionHandler.ItemUsedOnTileTriggerConditions(
							item, this, data, xt, yt, level.depth));
					return true;
				}
			}
		}
		return false;
	}

	public void hurt(Level level, int x, int y, int dmg) {
		if (random.nextInt(100) == 0)
			level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Apple"));

		int damage = level.getData(x, y) + dmg;
		int palmHealth = 20;
		if (Game.isMode("minicraft.settings.mode.creative")) dmg = damage = palmHealth;

		level.add(new SmashParticle(x * 16, y * 16));
		Sound.play("monsterhurt");

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (damage >= palmHealth) {
			level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, Items.get("Mature Coconut"));
			level.dropItem(x * 16 + 8, y * 16 + 8, 0, 2, Items.get("Green Coconut"));
			level.setTile(x, y, Tiles.get("Sand"));
			AchievementsDisplay.setAchievement("minicraft.achievement.woodcutter", true);
		} else {
			level.setData(x, y, damage);
		}
	}
}
