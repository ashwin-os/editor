package com.editor.datamodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.editor.AppRunner;
import com.editor.datamodel.DiffMatchPatch.Patch;
import com.editor.utils.diff.DiffUtility;

public class Document {

	private final int docId;

	private final String docName;

	private String docAsString;

	AtomicInteger nextRevisionId = new AtomicInteger(0);

	AtomicInteger lastUpdatedRevisionId = new AtomicInteger(-1);

	Map<Integer, Revision> revisionsMap = new HashMap<>();

	public Document(int id, String docName, String doc) {
		this.docId = id;
		this.docName = docName;
		this.docAsString = doc;

		ScheduledExecutorService ex1 = Executors.newScheduledThreadPool(1);
		ex1.scheduleWithFixedDelay(() -> updateDocPeriodically(), 200, 200,
				TimeUnit.MILLISECONDS);
	}

	private void updateDocPeriodically() {

		boolean isUpdated = false;
		for (int i = lastUpdatedRevisionId.get() + 1; i < nextRevisionId.get(); i++) {
			isUpdated = true;
			docAsString = DiffUtility.mergeChanges(docAsString, revisionsMap
					.get(i).getPatches());
			lastUpdatedRevisionId.incrementAndGet();
		}

		if (isUpdated)
			persistServerCopy();
	}

	private void persistServerCopy() {

		File serverCopy = new File(AppRunner.GLOBAL_PATH + "/server");
		try {
			serverCopy.createNewFile();
			FileWriter fooWriter = new FileWriter(serverCopy, false);
			fooWriter.write(docAsString);
			fooWriter.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public int getDocId() {
		return docId;
	}

	public String getDocName() {
		return docName;
	}

	public String getDoc() {
		return docAsString;
	}

	public Revision createRevision(LinkedList<Patch> patches) {
		int version = nextRevisionId.getAndIncrement();
		Revision rev = new Revision(version, patches);
		revisionsMap.put(version, rev);
		return rev;
	}

	public List<Revision> getRevisions(int from) {
		List<Revision> revisions = new ArrayList<Revision>();
		for (int i = from + 1; i < nextRevisionId.get(); i++) {
			revisions.add(revisionsMap.get(i));
		}
		return revisions;
	}

	public int getLatestRevisionId() {
		return nextRevisionId.get()-1;
	}
}
