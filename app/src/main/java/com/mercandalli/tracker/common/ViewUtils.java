package com.mercandalli.tracker.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Static methods for dealing with the {@link View}s.
 */
@SuppressWarnings("unused")
public final class ViewUtils {

    private ViewUtils() {
        // Non-instantiable.
    }

    @Nullable
    public static Drawable getSelectableItemBackground(@NonNull final Context context) {
        // Create an array of the attributes we want to resolve
        // using values from a theme
        // android.R.attr.selectableItemBackground requires API LEVEL 11
        final int[] attrs = new int[]{android.R.attr.selectableItemBackground /* index 0 */};

        // Obtain the styled attributes. 'themedContext' is a context with a
        // theme, typically the current Activity (i.e. 'this')
        final TypedArray ta = context.obtainStyledAttributes(attrs);

        // Now get the value of the 'listItemBackground' attribute that was
        // set in the theme used in 'themedContext'. The parameter is the index
        // of the attribute in the 'attrs' array. The returned Drawable
        // is what you are after
        final Drawable drawableFromTheme = ta.getDrawable(0 /* index */);

        // Finally free resources used by TypedArray
        ta.recycle();
        return drawableFromTheme;
    }
}
