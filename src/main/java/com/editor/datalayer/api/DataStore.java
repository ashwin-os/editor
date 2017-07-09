package com.editor.datalayer.api;

import java.util.LinkedList;
import java.util.List;

import com.editor.datamodel.Document;
import com.editor.datamodel.Revision;
import com.editor.datamodel.DiffMatchPatch.Patch;

public interface DataStore {

	public Document createDocument(String docName);

	public Document getDocumentByName(String docName);

	public Document getDocumentById(int docId);

	public Revision createRevision(int docId, LinkedList<Patch> patches);

	public List<Revision> getRevisions(int docId, int from);

	public int getLatestRevisionId(int docId);

}
