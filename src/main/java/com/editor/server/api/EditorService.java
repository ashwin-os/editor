package com.editor.server.api;

import java.util.LinkedList;

import com.editor.datamodel.Document;
import com.editor.datamodel.DocumentRep;
import com.editor.datamodel.DiffMatchPatch.Patch;

public interface EditorService {

	public DocumentRep getDocument(int docId, int myVersionId);
	
	public DocumentRep saveDocument(int docId, LinkedList<Patch> diff, int myVersion);

	public Document getDocumentByName(String docName);
	
	public Document createDocumentByName(String docName);
	
}
