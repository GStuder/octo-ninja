package net.kreatious.sandbox.ast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

/**
 * Parses a file into an abstract syntax tree for modification.
 * 
 * @author JStuder
 */
public abstract class AbstractJavaVisitor extends ASTVisitor {
    private CompilationUnit unit;

    /**
     * Reads the specified java file.
     * 
     * @param file
     *            the java file to parse
     * @throws IOException
     *             if an I/O error occurs
     */
    public void read(File file) throws IOException {
        try (InputStream sourceStream = FileUtils.openInputStream(file)) {
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setSource(IOUtils.toCharArray(sourceStream));
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            unit = (CompilationUnit) parser.createAST(null);
            unit.accept(this);
        }
    }

    /**
     * Modifies the specified java file.
     * 
     * @param file
     *            the java file to parse and modify.
     * @throws IOException
     *             if an I/O error occurs
     */
    public void modify(File file) throws IOException {
        try (InputStream sourceStream = FileUtils.openInputStream(file)) {
            Document source = new Document(IOUtils.toString(sourceStream));
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setSource(source.get().toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            unit = (CompilationUnit) parser.createAST(null);
            unit.recordModifications();
            unit.accept(this);
            unit.rewrite(source, JavaCore.getOptions()).apply(source);

            FileUtils.write(file, source.get());
        } catch (MalformedTreeException | BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Calculates the line number that corresponds to the first character in the
     * given syntax node in the source file.
     * <p>
     * The first line of the source file is numbered 1. Returns -1 if the
     * specified node does not correspond to any lines, and -2 if no line number
     * information is available for the source file.
     * 
     * @param node
     *            the abstract syntax tree node to calculate the line number for
     * @return the 1-based line number corresponding to the start of the node.
     */
    protected int getStartLineNumber(ASTNode node) {
        return unit.getLineNumber(node.getStartPosition());
    }

    /**
     * Calculates the line number that corresponds to the last character in the
     * given syntax node in the source file.
     * <p>
     * The first line of the source file is numbered 1. Returns -1 if the
     * specified node does not correspond to any lines, and -2 if no line number
     * information is available for the source file.
     * 
     * @param node
     *            the abstract syntax tree node to calculate the line number for
     * @return the 1-based line number corresponding to the end of the node.
     */
    protected int getEndLineNumber(ASTNode node) {
        return unit.getLineNumber(node.getStartPosition() + node.getLength());
    }
}
