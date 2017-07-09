package com.editor.client;

import java.util.LinkedList;

import com.editor.datamodel.Document;
import com.editor.datamodel.DocumentRep;
import com.editor.datamodel.Revision;
import com.editor.datamodel.DiffMatchPatch.Patch;
import com.editor.server.impl.EditorServiceImpl;
import com.editor.utils.diff.DiffUtility;

public class EditorClient {
	
	private int clientId = -1;
	
	private Document localDocCopy;

	private int localRevisionId = -1;

	private LinkedList<Patch> uncommitedDiff = new LinkedList<Patch>();

	public EditorClient(String docName, int id) {
		this.clientId = id;
		this.localDocCopy = EditorServiceImpl.getInstance().getDocumentByName(docName);
		localRevisionId = this.localDocCopy.getLatestRevisionId();
	}

	public boolean modifiedDoc(String updatedDoc) {
		LinkedList<Patch> changes = DiffUtility.createPatch(localDocCopy.getDoc(), updatedDoc);

		if (!changes.isEmpty()) {
			uncommitedDiff.addAll(changes);
			System.out.println("clientId: " + clientId + ", localRevisionId:" + localRevisionId + ", new changes: " + changes);
		}

		return syncChangesPeriodically();
	}

	public boolean syncChangesPeriodically() {
		DocumentRep latestChanges = null;
		
		if (!uncommitedDiff.isEmpty()) {
			latestChanges = EditorServiceImpl.getInstance().saveDocument(
					localDocCopy.getDocId(), uncommitedDiff, localRevisionId);
			localRevisionId = latestChanges.getCurrentRevisionId();
			uncommitedDiff = new LinkedList<Patch>();
		} else {
			latestChanges = EditorServiceImpl.getInstance().getDocument(
					localDocCopy.getDocId(), localRevisionId);

		}

		String prev = localDocCopy.getDoc();
		String finalDoc = prev;
		for (Revision revision : latestChanges.getLatestDiff()) {
			finalDoc = DiffUtility
					.mergeChanges(finalDoc, revision.getPatches());
		}

		localDocCopy = new Document(localDocCopy.getDocId(),
				localDocCopy.getDocName(), finalDoc);
		localRevisionId = latestChanges.getCurrentRevisionId();

		if (prev != finalDoc) {
			System.out.println("new copy " + localDocCopy.getDoc());
			return true;
		}

		return false;
	}

	public String displayDocument() {
		return localDocCopy.getDoc();
	}

}
