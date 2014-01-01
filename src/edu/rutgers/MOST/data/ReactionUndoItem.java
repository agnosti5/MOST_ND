package edu.rutgers.MOST.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;

import edu.rutgers.MOST.config.LocalConfig;
import edu.rutgers.MOST.presentation.EntryValidator;
import edu.rutgers.MOST.presentation.GraphicalInterface;
import edu.rutgers.MOST.presentation.GraphicalInterfaceConstants;

public class ReactionUndoItem implements UndoItem {
	
	private Integer id;
	private Integer row;
	private Integer column;
	private String oldValue;
	private String newValue;
	private String undoType;
	private String undoItemType;
	private String equationNames;
	private int oldSortColumnIndex;
	private int newSortColumnIndex;
	private SortOrder oldSortOrder;
	private SortOrder newSortOrder;
	private int addedColumnIndex;
	private int deletedColumnIndex;
	private ArrayList<Integer> addedMetabolites;
	private ArrayList<String> addedMetaboliteAbbr;
	private int tableCopyIndex;
	private ArrayList<String> oldMetaColumnNames;
	private ArrayList<String> newMetaColumnNames;
	private String columnName;
	private SBMLReactionEquation equn;
	private int maxMetab;
	private int maxMetabId;
	private ArrayList<String> pasteIds;
	private String oldLowerBound;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getColumn() {
		return column;
	}
	public void setColumn(Integer column) {
		this.column = column;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}	
	public String getUndoType() {
		return undoType;
	}
	public void setUndoType(String undoType) {
		this.undoType = undoType;
	}	
	public String getUndoItemType() {
		return undoItemType;
	}
	public void setUndoItemType(String undoItemType) {
		this.undoItemType = undoItemType;
	}
	public String getEquationNames() {
		return equationNames;
	}
	public void setEquationNames(String equationNames) {
		this.equationNames = equationNames;
	}
	public int getOldSortColumnIndex() {
		return oldSortColumnIndex;
	}
	public void setOldSortColumnIndex(int oldSortColumnIndex) {
		this.oldSortColumnIndex = oldSortColumnIndex;
	}
	public int getNewSortColumnIndex() {
		return newSortColumnIndex;
	}
	public void setNewSortColumnIndex(int newSortColumnIndex) {
		this.newSortColumnIndex = newSortColumnIndex;
	}
	public SortOrder getOldSortOrder() {
		return oldSortOrder;
	}
	public void setOldSortOrder(SortOrder oldSortOrder) {
		this.oldSortOrder = oldSortOrder;
	}
	public SortOrder getNewSortOrder() {
		return newSortOrder;
	}
	public void setNewSortOrder(SortOrder newSortOrder) {
		this.newSortOrder = newSortOrder;
	}	
	public int getAddedColumnIndex() {
		return addedColumnIndex;
	}
	public void setAddedColumnIndex(int addedColumnIndex) {
		this.addedColumnIndex = addedColumnIndex;
	}
	public int getDeletedColumnIndex() {
		return deletedColumnIndex;
	}
	public void setDeletedColumnIndex(int deletedColumnIndex) {
		this.deletedColumnIndex = deletedColumnIndex;
	}	
	public ArrayList<Integer> getAddedMetabolites() {
		return addedMetabolites;
	}
	public void setAddedMetabolites(ArrayList<Integer> addedMetabolites) {
		this.addedMetabolites = addedMetabolites;
	}	
	public ArrayList<String> getAddedMetaboliteAbbr() {
		return addedMetaboliteAbbr;
	}
	public void setAddedMetaboliteAbbr(ArrayList<String> addedMetaboliteAbbr) {
		this.addedMetaboliteAbbr = addedMetaboliteAbbr;
	}
	public int getTableCopyIndex() {
		return tableCopyIndex;
	}
	public void setTableCopyIndex(int tableCopyIndex) {
		this.tableCopyIndex = tableCopyIndex;
	}	
	public ArrayList<String> getOldMetaColumnNames() {
		return oldMetaColumnNames;
	}
	public void setOldMetaColumnNames(ArrayList<String> oldMetaColumnNames) {
		this.oldMetaColumnNames = oldMetaColumnNames;
	}
	public ArrayList<String> getNewMetaColumnNames() {
		return newMetaColumnNames;
	}
	public void setNewMetaColumnNames(ArrayList<String> newMetaColumnNames) {
		this.newMetaColumnNames = newMetaColumnNames;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}	
	public SBMLReactionEquation getEqun() {
		return equn;
	}
	public void setEqun(SBMLReactionEquation equn) {
		this.equn = equn;
	}	
	public int getMaxMetab() {
		return maxMetab;
	}
	public void setMaxMetab(int maxMetab) {
		this.maxMetab = maxMetab;
	}
	public int getMaxMetabId() {
		return maxMetabId;
	}
	public void setMaxMetabId(int maxMetabId) {
		this.maxMetabId = maxMetabId;
	}	
	public ArrayList<String> getPasteIds() {
		return pasteIds;
	}
	public void setPasteIds(ArrayList<String> pasteIds) {
		this.pasteIds = pasteIds;
	}		
	public String getOldLowerBound() {
		return oldLowerBound;
	}
	public void setOldLowerBound(String oldLowerBound) {
		this.oldLowerBound = oldLowerBound;
	}	
	
	public String createUndoDescription() {
		String undoDescription = "";
		if (this.undoType.equals(UndoConstants.TYPING)) {
			undoDescription = UndoConstants.TYPING + "'"
			+ this.newValue + "' in '" + this.columnName + "' row " + (this.row + 1);				
		} else if (this.undoType.equals(UndoConstants.REPLACE)) {
			undoDescription = UndoConstants.REPLACE;
		} else if (this.undoType.equals(UndoConstants.REPLACE_ALL)) {
			undoDescription = UndoConstants.REPLACE_ALL;	
		} else if (this.undoType.equals(UndoConstants.ADD_ROW)) {
			undoDescription = UndoConstants.ADD_ROW;
		} else if (this.undoType.equals(UndoConstants.ADD_COLUMN)) {
			undoDescription = UndoConstants.ADD_COLUMN_PREFIX + displayReactionsColumnNameFromIndex(this.addedColumnIndex, this.newMetaColumnNames) + UndoConstants.ADD_COLUMN_SUFFIX;	
		} else if (this.undoType.equals(UndoConstants.DELETE_ROW)) {
			undoDescription = UndoConstants.DELETE_ROW;	
		} else if (this.undoType.equals(UndoConstants.DELETE_COLUMN)) {
			undoDescription = UndoConstants.DELETE_COLUMN_PREFIX;
			undoDescription = UndoConstants.DELETE_COLUMN_PREFIX + displayReactionsColumnNameFromIndex(this.deletedColumnIndex, this.oldMetaColumnNames) + UndoConstants.DELETE_COLUMN_SUFFIX;								
		} else if (this.undoType.equals(UndoConstants.PASTE)) {
			undoDescription = UndoConstants.PASTE;	
		} else if (this.undoType.equals(UndoConstants.CLEAR_CONTENTS)) {
			undoDescription = UndoConstants.CLEAR_CONTENTS;	
		} else if (this.undoType.equals(UndoConstants.SORT)) {
			undoDescription = UndoConstants.SORT;
		} else if (this.undoType.equals(UndoConstants.UNSORT)) {
			undoDescription = UndoConstants.UNSORT;	
		}
		return undoDescription + UndoConstants.REACTION_UNDO_SUFFIX;
	}
	
	public void undo() {
		if (this.undoType.equals(UndoConstants.TYPING) || this.undoType.equals(UndoConstants.REPLACE)) {
			undoEntry();
		} else if (this.undoType.equals(UndoConstants.ADD_ROW)) {
			undoAddRow();
		} else if (this.undoType.equals(UndoConstants.ADD_COLUMN)) {
			undoAddColumn();
			copyTableUndoAction();			
		} else if (this.undoType.equals(UndoConstants.DELETE_COLUMN)) {
			undoDeleteColumn();
			copyTableUndoAction();
		} else if (this.undoType.equals(UndoConstants.PASTE) || this.undoType.equals(UndoConstants.REPLACE_ALL)) {
			copyTableUndoAction();
			Map<String, Object> reactionsIdRowMap = new HashMap<String, Object>();
			for (int i = 0; i < GraphicalInterface.reactionsTable.getRowCount(); i++) {
				reactionsIdRowMap.put((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN), i);
			}
			for (int i = 0; i < this.pasteIds.size(); i++) {
				int rowNum = Integer.valueOf(this.pasteIds.get(i));
				String rev = (String) GraphicalInterface.reactionsTable.getModel().getValueAt(rowNum, GraphicalInterfaceConstants.REVERSIBLE_COLUMN);
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(Integer.valueOf(this.pasteIds.get(i)))).setReversible(rev);
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(Integer.valueOf(this.pasteIds.get(i)))).writeReactionEquation();
			}
		} else if (this.undoType.equals(UndoConstants.CLEAR_CONTENTS) || this.undoType.equals(UndoConstants.DELETE_ROW)) {	
			copyTableUndoAction();
		}
		//System.out.println(LocalConfig.getInstance().getReactionEquationMap());
	}
	
	public void copyTableUndoAction() {
		int numCopied = LocalConfig.getInstance().getNumReactionTablesCopied();
		numCopied -= 2;
		LocalConfig.getInstance().setNumReactionTablesCopied(numCopied);
		GraphicalInterface.reactionsTable.setModel(LocalConfig.getInstance().getReactionsUndoTableModelMap().get(Integer.toString(numCopied + 1)));
	}
	
	public void redo() {
		if (this.undoType.equals(UndoConstants.TYPING) || this.undoType.equals(UndoConstants.REPLACE)) {
			redoEntry();
		} else if (this.undoType.equals(UndoConstants.ADD_ROW)) {
			redoAddRow();
		} else if (this.undoType.equals(UndoConstants.ADD_COLUMN)) {
			redoAddColumn();
			copyTableRedoAction();			
		} else if (this.undoType.equals(UndoConstants.DELETE_COLUMN)) {
			redoDeleteColumn();
			copyTableRedoAction();
		} else if (this.undoType.equals(UndoConstants.PASTE) || this.undoType.equals(UndoConstants.REPLACE_ALL)) {
			copyTableRedoAction();
			Map<String, Object> reactionsIdRowMap = new HashMap<String, Object>();
			for (int i = 0; i < GraphicalInterface.reactionsTable.getRowCount(); i++) {
				reactionsIdRowMap.put((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN), i);
			}
			for (int i = 0; i < this.pasteIds.size(); i++) {
				int rowNum = Integer.valueOf(this.pasteIds.get(i));
				String rev = (String) GraphicalInterface.reactionsTable.getModel().getValueAt(rowNum, GraphicalInterfaceConstants.REVERSIBLE_COLUMN);
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(Integer.valueOf(this.pasteIds.get(i)))).setReversible(rev);
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(Integer.valueOf(this.pasteIds.get(i)))).writeReactionEquation();
			}
		} else if (this.undoType.equals(UndoConstants.CLEAR_CONTENTS) || this.undoType.equals(UndoConstants.DELETE_ROW)) {
			copyTableRedoAction();
		}
		//System.out.println(LocalConfig.getInstance().getReactionEquationMap());
	}
	
	public void copyTableRedoAction() {
		int numCopied = LocalConfig.getInstance().getNumReactionTablesCopied();
		numCopied += 2;
		LocalConfig.getInstance().setNumReactionTablesCopied(numCopied);
		GraphicalInterface.reactionsTable.setModel(LocalConfig.getInstance().getReactionsUndoTableModelMap().get(Integer.toString(numCopied)));
	}
	
	public boolean undoEntry() {
		
		if (this.column == GraphicalInterfaceConstants.KO_COLUMN) {
			if (this.oldValue.toLowerCase().startsWith(GraphicalInterfaceConstants.VALID_TRUE_VALUES[0])) {
				this.oldValue = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];
			} else if (this.oldValue.toLowerCase().startsWith(GraphicalInterfaceConstants.VALID_FALSE_VALUES[0])) {
				this.oldValue = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
			}				
		}
		updateCellById(this.oldValue, this.id, this.column);
		
		if (this.column == GraphicalInterfaceConstants.REVERSIBLE_COLUMN) {
			EntryValidator validator = new EntryValidator();
			if (validator.validTrueEntry(this.oldValue)) {
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).setReversible(GraphicalInterfaceConstants.BOOLEAN_VALUES[1]);
			} else if (validator.validFalseEntry(this.oldValue)) {
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).setReversible(GraphicalInterfaceConstants.BOOLEAN_VALUES[0]);
			}			
			((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).writeReactionEquation();
			updateCellById(((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).equationAbbreviations, this.id, GraphicalInterfaceConstants.REACTION_EQUN_ABBR_COLUMN);
			updateCellById(((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).equationNames, this.id, GraphicalInterfaceConstants.REACTION_EQUN_NAMES_COLUMN);
			if (validator.validFalseEntry(this.newValue)) {
				if (this.oldLowerBound != null) {
					updateCellById(this.oldLowerBound, this.id, GraphicalInterfaceConstants.LOWER_BOUND_COLUMN);
				}				
			}
		}
		
		if (this.column.equals(GraphicalInterfaceConstants.REACTION_EQUN_ABBR_COLUMN)) {
		//if (this.column.equals(GraphicalInterfaceConstants.REACTION_EQUN_ABBR_COLUMN) && this.newValue != null && this.newValue.trim().length() > 0) {
			LocalConfig.getInstance().setMaxMetabolite(this.maxMetab);
			LocalConfig.getInstance().setMaxMetaboliteId(this.maxMetabId);
			Map<String, Object> metabolitesIdRowMap = new HashMap<String, Object>();
			for (int i = 0; i < GraphicalInterface.metabolitesTable.getRowCount(); i++) {
				metabolitesIdRowMap.put((String) GraphicalInterface.metabolitesTable.getModel().getValueAt(i, GraphicalInterfaceConstants.METABOLITE_ID_COLUMN), i);
			}
			ArrayList<String> addedAbbr = new ArrayList<String>();
			for (int i = 0; i < this.addedMetabolites.size(); i++) {
				String abbrev = (String) getKeyFromValue(LocalConfig.getInstance().getMetaboliteAbbreviationIdMap(), this.addedMetabolites.get(i)); 
				addedAbbr.add(abbrev);
				LocalConfig.getInstance().getMetaboliteAbbreviationIdMap().remove(abbrev);
				LocalConfig.getInstance().getMetaboliteUsedMap().remove(abbrev);
				String row = (metabolitesIdRowMap.get(Integer.toString(this.addedMetabolites.get(i)))).toString();
				
				int rowNum = Integer.valueOf(row);
				GraphicalInterface.metabolitesTable.getModel().setValueAt("", rowNum, GraphicalInterfaceConstants.METABOLITE_ABBREVIATION_COLUMN);
			}
			setAddedMetaboliteAbbr(addedAbbr);
		}
		
		return true;
	} 
	
	public boolean undoAddRow() {
		DefaultTableModel model = (DefaultTableModel) GraphicalInterface.reactionsTable.getModel();
		model.removeRow(this.row);
		int maxId = LocalConfig.getInstance().getMaxReactionId();
		LocalConfig.getInstance().setMaxReactionId(maxId - 1);
		if (LocalConfig.getInstance().getReactionEquationMap().containsKey(this.id)) {
			LocalConfig.getInstance().getReactionEquationMap().remove(this.id);
		}		
		
		return true;
		
	}
	
	public void undoAddColumn() {
		LocalConfig.getInstance().setReactionsMetaColumnNames(this.oldMetaColumnNames);
	}
	
	public void undoDeleteColumn() {
		LocalConfig.getInstance().setReactionsMetaColumnNames(this.oldMetaColumnNames);
	}
	
	public boolean redoEntry() {

		if (this.column == GraphicalInterfaceConstants.KO_COLUMN || this.column == GraphicalInterfaceConstants.REVERSIBLE_COLUMN) {
			if (this.newValue.toLowerCase().startsWith(GraphicalInterfaceConstants.VALID_TRUE_VALUES[0])) {
				this.newValue = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];
			} else if (this.newValue.toLowerCase().startsWith(GraphicalInterfaceConstants.VALID_FALSE_VALUES[0])) {
				this.newValue = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
			}				
		}
		updateCellById(this.newValue, this.id, this.column);
		
		if (this.column == GraphicalInterfaceConstants.REVERSIBLE_COLUMN) {
			EntryValidator validator = new EntryValidator();
			if (validator.validTrueEntry(this.newValue)) {
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).setReversible(GraphicalInterfaceConstants.BOOLEAN_VALUES[1]);
			} else if (validator.validFalseEntry(this.newValue)) {
				((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).setReversible(GraphicalInterfaceConstants.BOOLEAN_VALUES[0]);
				updateCellById("0.0", this.id, GraphicalInterfaceConstants.LOWER_BOUND_COLUMN);
			}			
			((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).writeReactionEquation();
			updateCellById(((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).equationAbbreviations, this.id, GraphicalInterfaceConstants.REACTION_EQUN_ABBR_COLUMN);
			updateCellById(((SBMLReactionEquation) LocalConfig.getInstance().getReactionEquationMap().get(this.id)).equationNames, this.id, GraphicalInterfaceConstants.REACTION_EQUN_NAMES_COLUMN);
		}
		
		return true;
	} 
	
	public void redoAddRow() {
		
	}
	
	public void redoAddColumn() {
		LocalConfig.getInstance().setReactionsMetaColumnNames(this.newMetaColumnNames);
	}
	
	public void redoDeleteColumn() {
		LocalConfig.getInstance().setReactionsMetaColumnNames(this.newMetaColumnNames);
	}
	
	public static String tableCopySuffix(int count) {
    	return new DecimalFormat("000").format(count);
    }
	
	public String displayReactionsColumnNameFromIndex(int columnIndex, ArrayList<String> metaColumnNames) {
		String columnName = "";
		if (columnIndex > GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES.length - 1) {
			columnName = metaColumnNames.get(columnIndex - (GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES.length));
		} else {
			columnName = GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES[columnIndex];
		}
		return columnName;
	}
	
	public String toString() {
		String undoString = "";
		if (this.undoType.startsWith(UndoConstants.TYPING) || this.undoType.startsWith(UndoConstants.REPLACE)) {
			
		} else if (this.undoType.equals(UndoConstants.ADD_ROW)) {
			
		} else if (this.undoType.equals(UndoConstants.ADD_COLUMN)) {
			undoString += this.oldMetaColumnNames + " ";
			undoString += this.newMetaColumnNames;
		} else if (this.undoType.equals(UndoConstants.DELETE_COLUMN)) {
			
		} else if (this.undoType.equals(UndoConstants.SORT)) {
			
		}
		return undoString;
		
	}
	
	public String toRedoString() {
		String redoString = "";
		if (this.undoType.startsWith(UndoConstants.TYPING) || this.undoType.startsWith(UndoConstants.REPLACE)) {
			
		} else if (this.undoType.equals(UndoConstants.ADD_ROW)) {
			
		} else if (this.undoType.equals(UndoConstants.ADD_COLUMN)) {
			
		} else if (this.undoType.equals(UndoConstants.DELETE_COLUMN)) {
			
		} else if (this.undoType.equals(UndoConstants.SORT)) {
			
		}
		return redoString;
		
	}
	
	public static Object getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}
	
	public static void updateCellById(String value, int id, int col) {
		Map<String, Object> reactionsIdRowMap = new HashMap<String, Object>();
		for (int i = 0; i < GraphicalInterface.reactionsTable.getRowCount(); i++) {
			reactionsIdRowMap.put((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN), i);
		}
		String row = (reactionsIdRowMap.get(Integer.toString(id))).toString();
		int rowNum = Integer.valueOf(row);
		GraphicalInterface.reactionsTable.getModel().setValueAt(value, rowNum, col);
	}
	
	public static void main(String[] args) {
		
	}
	
}

