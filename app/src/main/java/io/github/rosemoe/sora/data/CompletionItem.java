/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.sora.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.eurya.javaide.model.TextEdit;
import java.util.Comparator;
import java.util.List;

/**
 * The class used to save auto complete result items
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
public class CompletionItem {

    public static final Comparator<CompletionItem> COMPARATOR_BY_NAME =
            (p1, p2) -> p1.label.compareTo(p2.label);

    /** Icon for displaying in adapter */
    public Drawable icon;

    /** Text to commit when selected */
    public String commit;

    /** Text to display as title in adapter */
    public String label;

    /** Text to display as description in adapter */
    public String desc;

    /** Cursor offset in {@link CompletionItem#commit} */
    public int cursorOffset;

    public com.eurya.javaide.model.CompletionItem item;

    public CompletionItem(com.eurya.javaide.model.CompletionItem item) {
        this.item = item;
        this.label = item.label;
        this.commit = item.commitText;
        this.desc = item.detail;
        this.cursorOffset = item.cursorOffset;
    }

    public CompletionItem(String str, String desc) {
        this(str, desc, (Drawable) null);
    }

    public CompletionItem(String label, String commit, String desc) {
        this(label, commit, desc, null);
    }

    public CompletionItem(String label, String desc, Drawable icon) {
        this(label, label, desc, icon);
    }

    public CompletionItem(String label, String commit, String desc, Drawable icon) {
        this.label = label;
        this.commit = commit;
        this.desc = desc;
        this.icon = icon;
        cursorOffset = commit.length();
    }

    public CompletionItem shiftCount(int shiftCount) {
        return cursorOffset(commit.length() - shiftCount);
    }

    public void setCommitText(String text) {
        this.commit = text;
    }

    public CompletionItem cursorOffset(int offset) {
        if (offset < 0 || offset > commit.length()) {
            throw new IllegalArgumentException();
        }
        cursorOffset = offset;
        return this;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CompletionItem) {
            CompletionItem that = (CompletionItem) obj;
            if (!that.commit.equals(this.commit)) {
                return false;
            }
            if (!that.label.equals(this.label)) {
                return false;
            }
            if (that.cursorOffset != this.cursorOffset) {
                return false;
            }
            return true;
        }
        return false;
    }
}
