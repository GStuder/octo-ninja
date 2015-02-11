package net.kreatious.sandbox.ast;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Displays the declarations of variables within this source file.
 * 
 * @author JStuder
 */
public class VariableDeclarationVisitor extends AbstractJavaVisitor {
    public static void main(String[] args) throws IOException {
        new VariableDeclarationVisitor().read(new File(
                "src/main/java/net/kreatious/sandbox/ast/VariableDeclarationVisitor.java"));
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        int startingLineNumber = getStartLineNumber(node);
        int endingLineNumber = getEndLineNumber(node);

        System.out.println("Line " + startingLineNumber + "-" + endingLineNumber + ": " + node);
        return true;
    }
}
