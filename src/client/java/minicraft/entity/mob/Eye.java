package minicraft.entity.mob;

import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.entity.Teal;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.item.Items;

public class Eye extends EnemyMob {
	private static LinkedSprite[][][] sprites = new LinkedSprite[][][] {
		Mob.compileMobSpriteAnimations(0, 0, "eye"),
		Mob.compileMobSpriteAnimations(0, 2, "eye"),
		Mob.compileMobSpriteAnimations(0, 4, "eye"),
		Mob.compileMobSpriteAnimations(0, 6, "eye")
	};

	private int tealtime;
	private int cutime;

	/**
	 * Creates a Eye of a given level.
	 * @param lvl The Eye's level.
	 */
	public Eye(int lvl) {
		super(lvl, sprites, 6, true, 100, 45, 200);

		tealtime = 500 / (lvl + 5);
		cutime = tealtime;
	}

	@Override
	public void tick() {
		super.tick();

		if (skipTick()) return;

		Player player = getClosestPlayer();
		if (player != null && randomWalkTime == 0 && !Game.isMode("minicraft.settings.mode.creative")) { // Run if there is a player nearby, the Eye has finished their random walk, and gamemode is not creative.
			cutime--;

			int xd = player.x - x;
			int yd = player.y - y;
			if (xd * xd + yd * yd < 100 * 100) {
				if (cutime < 1) {
					level.add(new Teal(this, dir, lvl));
					cutime = tealtime;
				}
			}
		}
	}

	public void die() {
		int[] diffrands = { 20, 20, 30 };
		int[] diffvals = { 13, 18, 28 };
		int diff = Settings.getIdx("diff");

		int count = random.nextInt(3 - diff) + 1;
		int bookcount = random.nextInt(1) + 1;
		int rand = random.nextInt(diffrands[diff]);

		if (rand <= diffvals[diff])
			level.dropItem(x, y, count, Items.get("bone"), Items.get("wood"));
		else if (diff == 0 && rand >= 19) // Rare chance of 10 arrows on easy mode
			level.dropItem(x, y, 10, Items.get("wood"));
		else
			level.dropItem(x, y, bookcount, Items.get("Antidious"), Items.get("wood"));

		super.die();
	}
}
