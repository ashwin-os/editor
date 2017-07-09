package com.editor.datamodel;

import java.util.LinkedList;

import com.editor.datamodel.DiffMatchPatch.Patch;

public class Revision {

	long revisionId;

	LinkedList<Patch> patches;

	public Revision(long size, LinkedList<Patch> patch) {
		this.revisionId = size;
		this.patches = patch;
	}

	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public LinkedList<Patch> getPatches() {
		return patches;
	}

	public void setPatches(LinkedList<Patch> patches) {
		this.patches = patches;
	}

}
