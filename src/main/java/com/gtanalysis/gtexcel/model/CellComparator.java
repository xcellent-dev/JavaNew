/*
 */
package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.model.SortType;
import java.util.Comparator;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author nkabiliravi
 */
public class CellComparator implements Comparator<CellModel> {

    private final SortType sortType;

    public CellComparator(SortType sortType) {
        this.sortType = sortType;
    }

    public SortType getSortType() {
        return sortType;
    }

    @Override
    public int compare(CellModel o1, CellModel o2) {
        String cell1Value = o1.getValue() != null ? o1.getValue().toString() : "";
        String cell2Value = o2.getValue() != null ? o2.getValue().toString() : "";
        return sortType == SortType.DISCENDING ? cell2Value.compareTo(cell1Value) : cell1Value.compareTo(cell2Value);
    }

}
