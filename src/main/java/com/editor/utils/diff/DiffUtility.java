package com.editor.utils.diff;

import java.util.LinkedList;

import com.editor.datamodel.DiffMatchPatch;
import com.editor.datamodel.DiffMatchPatch.Diff;
import com.editor.datamodel.DiffMatchPatch.Patch;

public class DiffUtility {

	private static DiffMatchPatch dmp = new DiffMatchPatch();

	public static String mergeChanges(String doc, LinkedList<Patch> patches) {
		Object[] result = dmp.patch_apply(patches, doc);
		return result[0] + "";
	}
	
	public static LinkedList<Patch> createPatch(String text1, String text2)
	{
		return dmp.patch_make(text1, text2);
	}
	
	
	public static void main(String[] args) {
		DiffMatchPatch dmp = new DiffMatchPatch();
		LinkedList<Patch> patches = dmp.patch_make("The quick brown fox jumps over the lazy dog.", "That quick brown fox jumped over a lazy dog.");
		for (Patch patch : patches) {
			System.out.println("-------------------");
			LinkedList<Diff> diffs = patch.diffs;
			for (Diff diff : diffs) {
				System.out.println("***************");
				System.out.println(diff);
				System.out.println("***************");				
			}
			System.out.println("-------------------");

		}
		
		Object[] result = dmp.patch_apply(patches, "The quick brown fox jumps over the lazy dog.");
		System.out.println(result[0]);
	}
	
}
