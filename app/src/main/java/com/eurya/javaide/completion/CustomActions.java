package com.eurya.javaide.completion;

import com.eurya.javaide.model.CompletionItem;
import com.eurya.javaide.model.CompletionList;

import java.util.ArrayList;
import java.util.List;


/**
 * Convenience class for getting completions on custom actions like
 * Overriding a method
 */
public class CustomActions {
	
	private static final String TAG = CustomActions.class.getSimpleName();
	
	public static List<CompletionItem> addCustomActions(ParseTask task, String partial) {
		List<CompletionItem> items = new ArrayList<>();

		return items;
	}

	public static void addOverrideItem(CompletionList list) {
			CompletionItem item = new CompletionItem();
			item.action = CompletionItem.Kind.OVERRIDE;
			item.label = "Override methods";
			item.commitText = "";
			item.cursorOffset = 0;
			item.detail = "";
			list.items.add(0, item);
	}
}
