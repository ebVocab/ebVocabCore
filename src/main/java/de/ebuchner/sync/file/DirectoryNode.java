package de.ebuchner.sync.file;

import de.ebuchner.sync.SyncNode;
import de.ebuchner.sync.SyncNodeContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryNode extends FileNode implements SyncNodeContainer {

    List<FileNode> fileNodeList = new ArrayList<>();

    public DirectoryNode(FileNode parentNode, File directory) {
        super(parentNode, directory);
        refresh();
    }

    private void refresh() {
        fileNodeList = new ArrayList<>();
        File[] files = getFile().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    fileNodeList.add(new FileNode(this, file));
                else
                    fileNodeList.add(new DirectoryNode(this, file));
            }
        }
    }

    private String tokenOf(SyncNode syncNode) {
        FileNode fileNode = (FileNode) syncNode;
        StringBuilder token = new StringBuilder();
        File file = fileNode.getFile();
        if (file.isDirectory())
            token.append("dir\n");
        else
            token.append("file\n");
        token.append(file.getName());
        return token.toString();

    }

    @Override
    public List<? extends SyncNode> getChildren() {
        return fileNodeList;
    }

    @Override
    public Iterable<String> childTokens() {
        List<String> tokens = new ArrayList<>();
        for (SyncNode fileNode : getChildren()) {
            tokens.add(tokenOf(fileNode));
        }
        return tokens;
    }

    @Override
    public SyncNode findChildWithToken(String token) {
        for (SyncNode fileNode : getChildren()) {
            if (tokenOf(fileNode).equals(token))
                return fileNode;
        }
        return null;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public DirectoryNode asContainer() {
        return this;
    }
}
