package com.voyage.data.bo;

import java.util.List;

import com.voyage.battle.bo.ReplayBO;

/**
 * 打怪返回信息
 * */
public class MonsterChallengeBO {
	public ReplayBO replay;//战报
	public List<MapNodeTypeBO> nodeTypes;//大关卡列表
	public List<MapNodeBO> nodes;//小关卡列表

	public ReplayBO getReplay() {
		return replay;
	}

	public void setReplay(ReplayBO replay) {
		this.replay = replay;
	}

	public List<MapNodeTypeBO> getNodeTypes() {
		return nodeTypes;
	}

	public void setNodeTypes(List<MapNodeTypeBO> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}

	public List<MapNodeBO> getNodes() {
		return nodes;
	}

	public void setNodes(List<MapNodeBO> nodes) {
		this.nodes = nodes;
	}

}
