package minicraft.entity.mob;

import minicraft.core.io.Settings;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.item.Items;

public class Ghost extends EnemyMob {
	
	private static LinkedSprite[][][] sprites = new LinkedSprite[][][] {
		Mob.compileMobSpriteAnimations(0, 0, "ghost"),
		Mob.compileMobSpriteAnimations(0, 0, "ghost"),
		Mob.compileMobSpriteAnimations(0, 0, "ghost"),
		Mob.compileMobSpriteAnimations(0, 0, "ghost")
	};
	/**
	 * Creates a Ghost of the given level.
	 * @param lvl Ghost's level.
	 */
	public Ghost(int lvl) {
		super(lvl, sprites, 5, 100);
	}

	public void die() {
		if (Settings.get("diff").equals("minicraft.settings.difficulty.easy")) dropItem(2, 4, Items.get("cloth"));
		if (Settings.get("diff").equals("minicraft.settings.difficulty.normal")) dropItem(1, 3, Items.get("cloth"));
		if (Settings.get("diff").equals("minicraft.settings.difficulty.hard")) dropItem(1, 2, Items.get("cloth"));

		if (random.nextInt(60) == 2) {
			level.dropItem(x, y, Items.get("iron"));
		}

		if (random.nextInt(100) < 4) {
			level.dropItem(x, y, Items.get("Potato"));
		}

		super.die();
	}
}
