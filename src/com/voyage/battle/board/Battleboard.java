package com.voyage.battle.board;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyage.battle.enums.SideType;

/**
 * 战斗场地 
 *位置计算：左侧(x,y)+10; 右侧 (x,y)+20， 右侧为防守方
 *  ---------------- - - - - - > Y
 * (3,1)(2,1)(1,1)|(1,1)(2,1)(3,1)
 * (3,2)(2,2)(1,2)|(1,2)(2,2)(3,2)
 * (3,3)(2,3)(1,3)|(1,3)(2,3)(3,3)
 *                |
 *                v
 * 				  X
 * (x,y) -1: 空位置 0: 该单位已经死亡 1: 单位健在
 * 
 */
public class Battleboard {
	private final int x_max = 3;
	private final int y_max = 3;
	public static final int OFFSET_BASE = 10;
	private final int left_offset = OFFSET_BASE * SideType.LEFT.getV();
	private final int right_offset = OFFSET_BASE * SideType.RIGHT.getV();
	public final int capacity = x_max * y_max;
	public long createTime;
	private Map<Integer, BoardPoint> boardMap = new HashMap<Integer, BoardPoint>(capacity);

	public Battleboard() {
		for (int i = 0; i < capacity; i++) {
			boardMap.put(i + 1 + left_offset, new BoardPoint(i + 1 + left_offset));
		}
		for (int i = 0; i < capacity; i++) {
			boardMap.put(i + 1 + right_offset, new BoardPoint(i + 1 + right_offset));
		}
		createTime = System.currentTimeMillis();
	}

	/**查找某侧第1个活着的人*/
	public int findFirstAlive(int side) {
		return findAlive(side, 0);
	}

	/**查找某侧活着的人(先查找startIndex后的，再从头开始找)*/
	public int findAlive(int side, int startIndex) {
		int pos = stepAlivePos(side, startIndex);
		if (pos == -1 && startIndex > 0) {
			pos = stepAlivePos(side, 0);
		}
		return pos;
	}

	/**获得某侧所有活着的人*/
	public List<BoardPoint> getAlives(int side) {
		List<BoardPoint> alives = new ArrayList<BoardPoint>();
		for (BoardPoint bp : boardMap.values()) {
			if ((side == bp.pos / OFFSET_BASE) && bp.qAlive())
				alives.add(bp);
		}
		return alives;
	}

	/**获得某侧第N排或列所有活着的人*/
	public List<BoardPoint> getAlives(int side, boolean isRow, int n) {
		List<BoardPoint> alives = new ArrayList<BoardPoint>();
		for (BoardPoint bp : boardMap.values()) {
			if ((side == bp.pos / OFFSET_BASE) && (isRow ? posToPoint(bp.pos, side).x == n : posToPoint(bp.pos, side).y == n) && bp.qAlive())
				alives.add(bp);
		}
		return alives;
	}

	/**获得某侧所有的人*/
	public List<BoardPoint> getBoardPoints(int side) {
		List<BoardPoint> rt = new ArrayList<BoardPoint>();
		for (BoardPoint bp : boardMap.values()) {
			if (side == bp.pos / OFFSET_BASE)
				rt.add(bp);
		}
		return rt;
	}

	private int stepAlivePos(int side, int startIndex) {
		int pos = -1;
		int offset = side == SideType.LEFT.getV() ? left_offset : right_offset;
		for (int i = startIndex; i < capacity; i++) {
			if (boardMap.get(i + 1 + offset).qAlive())
				return i + 1 + offset;

		}
		return pos;
	}

	/**添加一个人员*/
	public void putBoardPoint(int pos) {
		boardMap.get(pos).alive();
	}

	/**返回一个位置*/
	public BoardPoint getBoardPoint(int pos) throws Exception {
		if (!(pos > left_offset && pos <= (capacity + right_offset)))
			throw new IllegalArgumentException("pos(" + pos + ") is out of range");
		return boardMap.get(pos);
	}

	/**将某人置为死亡*/
	public void dead(int pos) {
		boardMap.get(pos).dead();
	}

	public Point posToPoint(int pos, int side) {
		pos = side == SideType.LEFT.getV() ? pos - left_offset : pos - right_offset;
		Point p = new Point((int) Math.ceil(pos / (y_max + 0.0)), pos % y_max);
		if (p.x == 0)
			p.x = x_max;
		if (p.y == 0)
			p.y = y_max;
		return p;
	}

	public int pointToPos(Point p, int side) {
		int pos = (p.x - 1) * y_max + p.y;
		return side == SideType.LEFT.getV() ? pos + left_offset : pos + right_offset;
	}

	public static void main(String[] args) {
	}
}
