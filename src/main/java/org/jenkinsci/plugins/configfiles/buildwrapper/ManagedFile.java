/*
 The MIT License

 Copyright (c) 2011, Dominik Bartholdi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.jenkinsci.plugins.configfiles.buildwrapper;

import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.Util;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.ItemGroup;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ConfigFile;
import org.jenkinsci.plugins.configfiles.ConfigFiles;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;

/**
 * @author domi
 */
public class ManagedFile extends ConfigFile implements ExtensionPoint, Describable<ManagedFile>, Serializable {

    public String variable;

    /**
     * @param fileId the id of the file to be provided
     *
     * @since 2.12
     */
    @DataBoundConstructor
    public ManagedFile(String fileId) {
        super(fileId, null, false);
    }

    public ManagedFile(String fileId, String targetLocation, String variable, Boolean replaceTokens) {
        super(fileId, targetLocation, replaceTokens);
        this.variable = Util.fixEmptyAndTrim(variable);
    }

    public ManagedFile(String fileId, String targetLocation, String variable) {
        super(fileId, targetLocation, false);
        this.variable = Util.fixEmptyAndTrim(variable);
    }

    @DataBoundSetter
    public void setTargetLocation(String targetLocation) {
        this.targetLocation = Util.fixEmptyAndTrim(targetLocation);
    }

    public String getVariable() {
        return this.variable;
    }

    @DataBoundSetter
    public void setVariable(String variable) {
        this.variable = Util.fixEmptyAndTrim(variable);
    }

    @DataBoundSetter
    public void setReplaceTokens(Boolean replaceTokens) {
        this.replaceTokens = replaceTokens != null ? replaceTokens : false;
    }

    @Override
    public String toString() {
        return "[ManagedFile: id=" + getFileId() + ", targetLocation=" + getTargetLocation() + ", variable=" + variable + "]";
    }

    @Override
    public Descriptor<ManagedFile> getDescriptor() {
        return (DescriptorImpl) Jenkins.getActiveInstance().getDescriptorOrDie(getClass());
    }


    @Symbol("configFile")
    @Extension
    public static class DescriptorImpl extends Descriptor<ManagedFile> {
        @Override
        public String getDisplayName() {
            return "";
        }

        public ListBoxModel doFillFileIdItems(@AncestorInPath ItemGroup context) {
            ListBoxModel items = new ListBoxModel();
            items.add("please select", "");
            for (Config config : ConfigFiles.getConfigsInContext(context, null)) {
                items.add(config.name, config.id);
            }
            return items;
        }

    }
}

