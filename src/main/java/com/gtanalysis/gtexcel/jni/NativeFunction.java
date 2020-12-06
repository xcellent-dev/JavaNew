/*
 */
package com.gtanalysis.gtexcel.jni;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

/**
 *
 * @author nkabiliravi
 */
public class NativeFunction implements FreeRefFunction {

    private String name;

    private String description;

    private Method method;

    private final FunctionSet functionSet;

    public NativeFunction(FunctionSet functionSet) {
        this.functionSet = functionSet;
    }

    public NativeFunction(FunctionSet functionSet, String name, String description, Method method) {
        this.functionSet = functionSet;
        this.name = name;
        this.description = description;
        this.method = method;
    }

    public String getName() {
        return "native_" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (String.class.equals(method.getParameterTypes()[0].getComponentType())) {
            List<String> stringList = new ArrayList<>();
            if (args.length > 0 && args[0] instanceof AreaEval) {
                AreaEval areaEval = (AreaEval) args[0];
                int firstColumn = areaEval.getFirstColumn();
                int firstRow = areaEval.getFirstRow();
                int lastColumn = areaEval.getLastColumn();
                int lastRow = areaEval.getLastRow();
                for (int i = firstRow; i <= lastRow; i++) {
                    for (int j = firstColumn; j <= lastColumn; j++) {
                        ValueEval valueEval = areaEval.getAbsoluteValue(i, j);
                        if (valueEval instanceof StringValueEval) {
                            StringValueEval stringValueEval = (StringValueEval) valueEval;
                            stringList.add(stringValueEval.getStringValue());
                        } else if(valueEval instanceof NumericValueEval) {
                            NumericValueEval numericValueEval = (NumericValueEval) valueEval;
                            stringList.add(numericValueEval.getNumberValue() + "");                            
                        } else {
                            stringList.add("");
                        }
                    }
                }
            }
            String[] param = new String[stringList.size()];
            stringList.toArray(param);
            try {
                Object resultVal = method.invoke(this.functionSet, new Object[]{param});
                return new StringEval(resultVal.toString());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(NativeFunction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else if (Double.class.isAssignableFrom(method.getParameterTypes()[0].getComponentType())
                || method.getParameterTypes()[0].getComponentType().isPrimitive()
                && "double".equals(method.getParameterTypes()[0].getComponentType().getName())) {

            List<Double> doubleList = new ArrayList<>();
            if (args.length > 0 && args[0] instanceof AreaEval) {
                AreaEval areaEval = (AreaEval) args[0];
                int firstColumn = areaEval.getFirstColumn();
                int firstRow = areaEval.getFirstRow();
                int lastColumn = areaEval.getLastColumn();
                int lastRow = areaEval.getLastRow();
                for (int i = firstRow; i <= lastRow; i++) {
                    for (int j = firstColumn; j <= lastColumn; j++) {
                        ValueEval valueEval = areaEval.getAbsoluteValue(i, j);
                        if (valueEval instanceof NumericValueEval) {
                            NumericValueEval numericValueEval = (NumericValueEval) valueEval;
                            doubleList.add(numericValueEval.getNumberValue());
                        }
                    }
                }
            }
            if (Double.class.isAssignableFrom(method.getReturnType())
                    || method.getReturnType().isPrimitive()
                    && "double".equals(method.getReturnType().getName())) {
                Double[] doubleArr = new Double[doubleList.size()];
                doubleList.toArray(doubleArr);
                double[] param = ArrayUtils.toPrimitive(doubleArr);
                try {
                    double resultVal = (double) method.invoke(this.functionSet, new Object[]{param});
                    return new NumberEval(resultVal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(NativeFunction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else {
                Double[] doubleArr = new Double[doubleList.size()];
                doubleList.toArray(doubleArr);
                double[] param = ArrayUtils.toPrimitive(doubleArr);
                try {
                    Object resultVal = method.invoke(this.functionSet, param);
                    return new StringEval(resultVal.toString());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(NativeFunction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        return new StringEval("");
    }

}
