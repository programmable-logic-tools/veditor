package net.sourceforge.veditor.editor;

import java.util.ArrayList;

import net.sourceforge.veditor.VerilogPlugin;
import net.sourceforge.veditor.preference.PreferenceStrings;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class MarkSelectionOccurences implements ISelectionChangedListener {
	protected HdlEditor fEditor;
	protected ArrayList<Annotation> annotationArray;
	protected IAnnotationModel lastModel;
	final protected int maxHits = 100;
	
	public MarkSelectionOccurences(HdlEditor editor) {
		fEditor = editor;
		annotationArray = new ArrayList<Annotation>();
		lastModel = null;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		
		// remove all items in the list
		if (lastModel != null) {
			int index = 0;
			for (index = 0; index < annotationArray.size(); index++) {
				Annotation annotation = annotationArray.get(index);
				Position pos = lastModel.getPosition(annotation);
				if (pos != null) {
					pos.delete();
					lastModel.removeAnnotation(annotation);
				}
			}
			lastModel = null;
			annotationArray.clear();
		}
		
		IAnnotationModel model = fEditor.getDocumentProvider().getAnnotationModel( fEditor.getEditorInput() );
		lastModel = model;
		
		if (selection instanceof TextSelection) { 
			// ToDo: It should consider variable scope and read or write.
			TextSelection textSelection = (TextSelection)selection;
			if ((textSelection.getLength() > 1) &&
					(VerilogPlugin.getPreferenceBoolean( PreferenceStrings.MARK_SELECTION_OCCURENCES ))) { // skip single character selections
				String text = fEditor.getViewer().getDocument().get();
				String selText = textSelection.getText();
				int length = selText.length();
				ArrayList<Integer> findList = new ArrayList<Integer>();
				
				// search for all occurences and annotate them
				int lastIndex = 0;
				do {
					lastIndex = text.indexOf(selText,lastIndex);
					if( lastIndex != -1){
						if (isIdentifier(text, length, lastIndex)) {
							findList.add(lastIndex);
						}
						lastIndex+=length;
						// stop on too much hits
						if (findList.size() >= maxHits) {
							break;
						}
					}
				} while(lastIndex != -1);
				
				// for single finds do not highlight
				if ((findList.size() > 1) && (findList.size() < maxHits)) {
					for (int i=0;i < findList.size(); i++) {
						Annotation annotation = new Annotation( "net.sourceforge.veditor.occurrences", false, "Description" );
						model.addAnnotation( annotation, new Position( findList.get(i), textSelection.getLength() ) );
						annotationArray.add(annotation);
					}
				}
			}
		}
	}

	/**
	 * test identifier.
	 * note: occurrence must be identifier
	 */
	private boolean isIdentifier(String text, int length, int index) {
		try {
			char prev = text.charAt(index - 1);
			char next = text.charAt(index + length);
			for (int i = 0; i < length; i++) {
				if (Character.isJavaIdentifierPart(text.charAt(index + i)) == false) {
					return false;
				}
			}
			// avoid part of other identifier
			if (Character.isJavaIdentifierPart(prev))
				return false;
			if (Character.isJavaIdentifierPart(next))
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
}
