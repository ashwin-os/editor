package com.editor.datamodel;

import java.util.List;

public class DocumentRep {

	private final long docId;
	private final int currentRevisionId;
	private final List<Revision> revisionsList;

	public DocumentRep(long id, int revision, List<Revision> diffs) {
		this.docId = id;
		this.currentRevisionId = revision;
		this.revisionsList = diffs;
	}

	public long getDocId() {
		return docId;
	}

	public int getCurrentRevisionId() {
		return currentRevisionId;
	}

	public List<Revision> getLatestDiff() {
		return revisionsList;
	}

}
