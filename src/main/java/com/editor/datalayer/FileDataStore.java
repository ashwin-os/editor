package com.editor.datalayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.editor.datalayer.api.DataStore;
import com.editor.datamodel.Document;
import com.editor.datamodel.Revision;
import com.editor.datamodel.DiffMatchPatch.Patch;

public class FileDataStore implements DataStore {

	AtomicInteger globalDocId = new AtomicInteger(0);
	Map<Integer, Document> docIdToDocMap = new HashMap<Integer, Document>();
	Map<String, Document> docNameToDocMap = new HashMap<String, Document>();

	@Override
	public Document createDocument(String docName) {
		int docId = globalDocId.getAndIncrement();
		Document doc = new Document(docId, docName, "");
		docIdToDocMap.put(docId, doc);
		docNameToDocMap.put(docName, doc);
		return doc;
	}

	@Override
	public Document getDocumentByName(String docName) {
		return docNameToDocMap.containsKey(docName) ? docNameToDocMap.get(docName) : createDocument(docName);
	}

	@Override
	public Document getDocumentById(int docId) {
		return docIdToDocMap.get(docId);
	}

	@Override
	public Revision createRevision(int docId, LinkedList<Patch> patches) {
		return getDocumentById(docId).createRevision(patches);
	}

	@Override
	public List<Revision> getRevisions(int docId, int from) {
		return getDocumentById(docId).getRevisions(from);
	}

	@Override
	public int getLatestRevisionId(int docId) {
		return getDocumentById(docId).getLatestRevisionId();
	}

}
