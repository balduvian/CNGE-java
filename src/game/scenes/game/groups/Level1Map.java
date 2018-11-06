package game.scenes.game.groups;

import cnge.core.Block;
import cnge.core.Entity;
import cnge.core.Map;
import cnge.core.Map.Access;
import cnge.core.MapBehavior;
import cnge.core.group.MapGroup;
import cnge.graphics.Camera;
import cnge.graphics.Transform;

public class Level1Map extends MapGroup<Level1Map._Level1Map>{

	public class _Level1Map extends Map {

		public _Level1Map(MapGroup<?> g, float x, float y, int l, Access a, float s, int[][] t) {
			super(g, x, y, l, a, s, t);
		}
		
	}
	
	public Level1Map(Class<_Level1Map> mt, int mx, MapBehavior<Level1Map, _Level1Map> bh, String ip, Access as, float ts, Block[] bs) {
		super(mt, mx, bh, ip, as, ts, bs);
	}
}
