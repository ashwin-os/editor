package com.editor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.editor.client.EditorClient;
import com.editor.datamodel.Document;
import com.editor.datamodel.DiffMatchPatch.Patch;
import com.editor.server.impl.EditorServiceImpl;
import com.editor.utils.diff.DiffUtility;

public class AppRunner {
	
	public static String GLOBAL_PATH = "/tmp";

	public static void main(String[] args) {

		if (args.length < 2) {
			System.out
					.println("Run as java -cp <classpath> AppRunner <localpath> <num-clients> \nExample : java -cp /home/ashwin/editor-0.0.1-SNAPSHOT.jar AppRunner /home/ashwin 3");
		return;
		}
		String path = args[0];
		GLOBAL_PATH = path;
		int nummClients = Integer.parseInt(args[1]);
		try {

			startServer(path);
			System.out.println("Starting, Hold on");
			Thread.sleep(3000);
			startClients(path, nummClients);
			System.out.println("Started, Please open files(in gedit) in location :" + path + " and start modifying client files for interactive editing");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void startServer(String path) throws Exception {

		File f = new File(path + "/server");
		loadDocument(f, true);
		String context = new Scanner(f).useDelimiter("\\Z").next();

		Document localDocCopy = EditorServiceImpl.getInstance()
				.getDocumentByName("doc1");
		LinkedList<Patch> changes = DiffUtility.createPatch("", context);
		EditorServiceImpl.getInstance().saveDocument(localDocCopy.getDocId(),
				changes, -1);
	}

	private static void startClients(String path, int nummClients)
			throws Exception {
		ScheduledExecutorService ex1 = Executors.newScheduledThreadPool(3);

		for (int i = 0; i < nummClients; i++) {
			String clientFile = "/client" + i;
			EditorClient cl = new EditorClient("doc1", i);
			File clientCopy = new File(path + clientFile);

			loadDocument(clientCopy, false);
			writeToDisk(clientCopy, cl.displayDocument());
			ex1.scheduleAtFixedRate(() -> syncLocalCopy(path, clientFile, cl, clientCopy), 3, 3, TimeUnit.SECONDS);
		}
	}

	private static void syncLocalCopy(String path, String clientFile, EditorClient cl, File clientCopy) {
		try {
			String context = new Scanner(clientCopy).useDelimiter("\\Z").next();
			boolean hasChanged = cl.modifiedDoc(context.toString());

			if (hasChanged) {
				writeToDisk(new File(path	+ clientFile), cl.displayDocument());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadDocument(File f, boolean isServer) throws IOException {
		if (f.createNewFile() && isServer) {
			writeToDisk(f, "Welcome, Start Writing Something..");
		}
	}
	
	private static void writeToDisk(File path, String content) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new FileWriter(path, false));
		out.write(content);
		out.close();
	}
}
