/*
 * This file is part of JOP, the Java Optimized Processor
 *   see <http://www.jopdesign.com/>
 *
 * Copyright (C) 2011, Stefan Hepp (stefan@stefant.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jopdesign.jcopter.inline;

import com.jopdesign.common.MethodInfo;
import com.jopdesign.common.config.BooleanOption;
import com.jopdesign.common.config.Config;
import com.jopdesign.common.config.EnumOption;
import com.jopdesign.common.config.OptionGroup;
import com.jopdesign.common.config.StringOption;
import com.jopdesign.jcopter.JCopter;

import java.util.List;

/**
 * @author Stefan Hepp (stefan@stefant.org)
 */
public class InlineConfig {

    public enum JVMInline { NONE, SAFE, ALL }

    public static final StringOption EXCLUDE =
            new StringOption("exclude", "Comma separated list of methods (without descriptor), classes and packages to exclude from inlining.", true);

    public static final BooleanOption ALLOW_CODEMODIFY =
            new BooleanOption("allow-codemodify", "Allow making methods public and renaming methods if required for inlining.", true);

    public static final BooleanOption SKIP_NP_CHECKS =
            new BooleanOption("skip-np-checks", "Do not generate any nullpointer checks", false);

    public static final EnumOption<JVMInline> INLINE_JVM =
            new EnumOption<JVMInline>("jvm-calls",
                    "Allow inlining of JVM calls: disabled, only if result is verifiable, all calls",
                    JVMInline.SAFE);

    public static final BooleanOption INLINE_LIBARIES =
            new BooleanOption("inline-libs", "Allow inlining of library code", false);

    private final JCopter jcopter;
    private final OptionGroup options;
    private final List<String> ignorePrefix;

    public static void registerOptions(OptionGroup options) {
        options.addOption(EXCLUDE);
        options.addOption(ALLOW_CODEMODIFY);
        options.addOption(SKIP_NP_CHECKS);
        options.addOption(INLINE_JVM);
        options.addOption(INLINE_LIBARIES);
    }

    public InlineConfig(JCopter jcopter, OptionGroup options) {
        this.jcopter = jcopter;
        this.options = options;
        this.ignorePrefix = Config.splitStringList(options.getOption(EXCLUDE));
    }

    public boolean allowChangeAccess() {
        return options.getOption(ALLOW_CODEMODIFY);
    }

    public boolean allowRename() {
        // TODO currently not supported by InlineHelper.prepareInvoke
        // return options.getOption(ALLOW_CODEMODIFY);
        return false;
    }

    public boolean doExcludeInvoker(MethodInfo invoker) {
        return false;
    }

    public boolean doExcludeInvokee(MethodInfo invokee) {
        String className = invokee.getClassName();

        // We never inline WCA targets, else they might be removed and are not available for analysis
        for (MethodInfo target : jcopter.getJConfig().getWCATargets()) {
            if (target.equals(invokee)) {
                return true;
            }
        }

        // NOTICE maybe separate configs for ignore from and ignore to?
        for (String prefix : ignorePrefix) {
            if ( className.startsWith(prefix+".") || className.equals(prefix)
                 || prefix.equals(className+"."+invokee.getShortName())
                 || prefix.equals(className + "#" +invokee.getShortName())) {
                return true;
            }
        }
        return false;
    }

    public boolean skipNullpointerChecks() {
        return options.getOption(SKIP_NP_CHECKS);
    }

    public JVMInline doInlineJVMCalls() {
        return options.getOption(INLINE_JVM);
    }

    public boolean doInlineLibraries() {
        return options.getOption(INLINE_LIBARIES);
    }
}
