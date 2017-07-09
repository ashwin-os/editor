package com.editor.server.impl;

import java.util.LinkedList;

import com.editor.datalayer.FileDataStore;
import com.editor.datalayer.api.DataStore;
import com.editor.datamodel.Document;
import com.editor.datamodel.DocumentRep;
import com.editor.datamodel.DiffMatchPatch.Patch;
import com.editor.server.api.EditorService;

public class EditorServiceImpl implements EditorService {

	private static EditorService service = new EditorServiceImpl();

	private static DataStore ds = new FileDataStore();

	public static EditorService getInstance() {
		return service;
	}

	@Override
	public DocumentRep getDocument(int docId, int myVersionId) {

		return new DocumentRep(docId, ds.getLatestRevisionId(docId),
				ds.getRevisions(docId, myVersionId));
	}

	@Override
	public DocumentRep saveDocument(int docId, LinkedList<Patch> diff,
			int myVersion) {

		ds.createRevision(docId, diff);
		return getDocument(docId, myVersion);
	}

	@Override
	public Document getDocumentByName(String docName) {

		return ds.getDocumentByName(docName);
	}

	@Override
	public Document createDocumentByName(String docName) {

		return ds.createDocument(docName);
	}

}
