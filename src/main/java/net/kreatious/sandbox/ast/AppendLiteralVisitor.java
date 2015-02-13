package net.kreatious.sandbox.ast;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * Modifies the string literals within this source file by prepending ./
 * 
 * @author JStuder
 */
public class AppendLiteralVisitor extends AbstractJavaVisitor {
    public static void main(String[] args) throws IOException {
        new AppendLiteralVisitor()
                .modify(new File("src/main/java/net/kreatious/sandbox/ast/AppendLiteralVisitor.java"));
    }

    @Override
    public boolean visit(StringLiteral node) {
        node.setLiteralValue("./" + node.getLiteralValue());
        return true;
    }
}
