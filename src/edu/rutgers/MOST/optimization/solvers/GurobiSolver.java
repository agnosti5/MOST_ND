package edu.rutgers.MOST.optimization.solvers;

import java.awt.HeadlessException;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javassist.Modifier;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import edu.rutgers.MOST.config.LocalConfig;
import edu.rutgers.MOST.data.Solution;
import edu.rutgers.MOST.optimization.GDBB.GDBB;
import edu.rutgers.MOST.presentation.GraphicalInterface;
import edu.rutgers.MOST.presentation.GraphicalInterfaceConstants;

public class GurobiSolver extends Solver {
	
	//static Logger log = Logger.getLogger(GurobiSolver.class);
	
	private URLClassLoader classLoader;
	private Class<?> grbClass;
		
	private Class<?> envClass;
	private Class<?> modelClass;
	private Object env;
	
	private Object model;

	private ArrayList<Object> vars = new ArrayList<Object>();

	private static boolean isAbort = false;
	
	public static void main(String[] args) {
		GurobiSolver gurobiSolver = new GurobiSolver();
	}
	public static void setAbort(boolean isAbort) {
		GurobiSolver.isAbort = isAbort;
	}
	
	public GurobiSolver() {		
		try {
			System.out.println(GraphicalInterface.getGurobiPath());
			File gurobiJARFile = new File(GraphicalInterface.getGurobiPath());
			classLoader = URLClassLoader.newInstance(new URL[]{ gurobiJARFile.toURI().toURL() });
			grbClass = classLoader.loadClass("gurobi.GRB");
			
			envClass = classLoader.loadClass("gurobi.GRBEnv");
			Constructor<?> envConstr = envClass.getConstructor(new Class[]{});
			env = envConstr.newInstance(new Object[]{});
			
			Class<?> grbDoubleParam = classLoader.loadClass("gurobi.GRB$DoubleParam");
			Class<?> grbIntParam = classLoader.loadClass("gurobi.GRB$IntParam");	
			Enum[] grbDoubleParamConstants = (Enum[]) grbDoubleParam.getEnumConstants();
			Enum[] grbIntParamConstants = (Enum[]) grbIntParam.getEnumConstants();
			Method envSetDoubleMethod = envClass.getMethod("set", new Class[]{ grbDoubleParam, double.class });
			Method envSetIntMethod = envClass.getMethod("set", new Class[]{ grbIntParam, int.class });
			
			for (int i = 0; i < grbDoubleParamConstants.length; i++) {
				if (grbDoubleParamConstants[i].name().equals("FeasibilityTol")) {
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0E-9 });
				} else if (grbDoubleParamConstants[i].name().equals("IntFeasTol")) {
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0E-9 });
				}
				/*
				switch (grbDoubleParamConstants[i].name()) {
				case "FeasibilityTol":
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0E-9 });
					break;
				case "IntFeasTol":
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0E-9 });
					break;
				}
				*/
			}

			for (int i = 0; i < grbIntParamConstants.length; i++) {
				if (grbIntParamConstants[i].name().equals("OutputFlag")) {
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], 0 });
				}
				/*
				switch (grbIntParamConstants[i].name()) {
				case "OutputFlag":
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], 0 });
					break;
				}
				*/
			}
			
			modelClass = classLoader.loadClass("gurobi.GRBModel");
			Constructor<?> modelConstr = modelClass.getConstructor(new Class[]{ envClass });
		    model = modelConstr.newInstance(new Object[]{ env });			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			try {
				Class<?> grbExceptionClass = classLoader.loadClass("gurobi.GRBException");
				Method grbExceptionGetErrorCodeMethod = grbExceptionClass.getMethod("getErrorCode", null);
				//int errorCode = (int) grbExceptionGetErrorCodeMethod.invoke(e, null);
				int errorCode = (Integer) grbExceptionGetErrorCodeMethod.invoke(e, null);
				
				if (errorCode == grbClass.getDeclaredField("ERROR_NO_LICENSE").getInt(null)) {
					//GraphicalInterface.getTextInput().setVisible(false);
					LocalConfig.getInstance().hasValidGurobiKey = false;
					GraphicalInterface.outputTextArea.setText("ERROR: No validation file - run 'grbgetkey' to refresh it.");
					Object[] options = {"    OK    "};
					int choice = JOptionPane.showOptionDialog(null, 
							"ERROR: No validation file - run 'grbgetkey' to refresh it.", 
							GraphicalInterfaceConstants.GUROBI_KEY_ERROR_TITLE, 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, options, options[0]);
//					if (choice == JOptionPane.YES_OPTION) {
//						try{
//							//Process p;
//							//p = Runtime.getRuntime().exec("cmd /c start cmd");
	//
//						}catch(Exception e2){}
	//
//					}
					/*
					if (choice == JOptionPane.NO_OPTION) {

					}
					 */
				}
				else {
					handleGurobiException();
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void abort() {
		isAbort = true;
	}
	
	@Override
	public void addConstraint(Map<Integer, Double> map, ConType con,
			double value) {		
		try {
			Class<?> grbLinExprClass;
			grbLinExprClass = classLoader.loadClass("gurobi.GRBLinExpr");
			Class<?> grbVarClass = classLoader.loadClass("gurobi.GRBVar");
			Object expr = grbLinExprClass.newInstance();
			Method exprAddTermMethod = grbLinExprClass.getMethod("addTerm", new Class[]{ double.class, grbVarClass });
			
			Set<Entry<Integer, Double>> s = map.entrySet();
			Iterator<Entry<Integer, Double>> it = s.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> m = it.next();
				int key = m.getKey();
				Double v = m.getValue();
				exprAddTermMethod.invoke(expr, new Object[]{ v, this.vars.get(key) });
			}
			
			Method modelAddConstrMethod = modelClass.getMethod("addConstr", new Class[]{ grbLinExprClass, char.class, double.class, String.class});
			modelAddConstrMethod.invoke(model, new Object[]{expr, getGRBConType(con), value, null});			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void enable() {
		isAbort = false;
	}

	public void finalize() {
		// Not guaranteed to be invoked
		Method modelDisposeMethod;
		try {
			modelDisposeMethod = modelClass.getMethod("dispose", null);
			modelDisposeMethod.invoke(model, null);

			Method envDisposeMethod = envClass.getMethod("dispose", null);
			envDisposeMethod.invoke(model, null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			handleGurobiException();
		}		
	}

	private char getGRBConType(ConType type) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		switch (type) {
		case LESS_EQUAL:
			return grbClass.getDeclaredField("LESS_EQUAL").getChar(null);
		case EQUAL:
			return grbClass.getDeclaredField("EQUAL").getChar(null);
		case GREATER_EQUAL:
			return grbClass.getDeclaredField("GREATER_EQUAL").getChar(null);
		default:
			return grbClass.getDeclaredField("LESS_EQUAL").getChar(null);
		}
	}

	private int getGRBObjType(ObjType type) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		switch (type) {
		case Minimize:
			return grbClass.getDeclaredField("MINIMIZE").getInt(null);
		case Maximize:
			return grbClass.getDeclaredField("MAXIMIZE").getInt(null);
		default:
			return grbClass.getDeclaredField("MINIMIZE").getInt(null);
		}
	}

	private char getGRBVarType(VarType type) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		switch (type) {
		case CONTINUOUS:
			return grbClass.getDeclaredField("CONTINUOUS").getChar(null);
		case BINARY:
			return grbClass.getDeclaredField("BINARY").getChar(null);
		case INTEGER:
			return grbClass.getDeclaredField("INTEGER").getChar(null);
		case SEMICONT:
			return grbClass.getDeclaredField("SEMICONT").getChar(null);
		case SEMIINT:
			return grbClass.getDeclaredField("SEMIINT").getChar(null);
		default:
			return grbClass.getDeclaredField("CONTINUOUS").getChar(null);
		}
	}

	@Override
	public String getName() {
		return "GurobiSolver";
	}

	public ArrayList<Double> getSoln() {
		ArrayList<Double> soln = new ArrayList<Double>(vars.size());		
		try {
			Class<?> grbDoubleAttr = classLoader.loadClass("gurobi.GRB$DoubleAttr");

			Enum[] grbDoubleAttrConstants = (Enum[]) grbDoubleAttr.getEnumConstants();
			Enum xAttr = null;
			for (int i = 0; i < grbDoubleAttrConstants.length; i++)
				if (grbDoubleAttrConstants[i].name() == "X") {
					xAttr = grbDoubleAttrConstants[i];
					break;
				}
			Class<?> varClass;
			varClass = classLoader.loadClass("gurobi.GRBVar");
			Method varGetMethod = varClass.getMethod("get", new Class[]{ grbDoubleAttr });
			
			if (this.vars.size() != 0) {
				for (int i = 0; i < this.vars.size(); i++) {
					soln.add((Double) varGetMethod.invoke(this.vars.get(i), xAttr));
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return soln;
	}
	
	private void handleGurobiException() {
		Method envGetErrorMsgMethod;
		try {
			envGetErrorMsgMethod = envClass.getMethod("getErrorMsg", null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public double optimize() {		
		try {
//			Callback logic
			Method modelGetVarsMethod = modelClass.getMethod("getVars", null);
			final Object[] vars = (Object[]) modelGetVarsMethod.invoke(model, null);
			final Class<?> grbCallbackClass = classLoader.loadClass("gurobi.GRBCallback");
			final Class<?> grbVarClass = classLoader.loadClass("gurobi.GRBVar");
			
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass(grbCallbackClass);
			factory.setFilter(
					new MethodFilter() {
						@Override
						public boolean isHandled(Method method) {
							return Modifier.isAbstract(method.getModifiers());
						}
					}
					);

			MethodHandler handler = new MethodHandler() {

				@Override
				public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
					if (isAbort) {
						Method grbCallbackAbortMethod = grbCallbackClass.getDeclaredMethod("abort", null);
						grbCallbackAbortMethod.setAccessible(true);
						grbCallbackAbortMethod.invoke(self, null);
					}
					ClassLoader classLoader = grbCallbackClass.getClassLoader();

					Class grbClass = classLoader.loadClass("gurobi.GRB");
					Field[] asdf = grbClass.getDeclaredFields();
					
					Field whereField = grbCallbackClass.getDeclaredField("where");
					whereField.setAccessible(true);
					
					int where = whereField.getInt(self);
					if (where == grbClass.getDeclaredField("CB_MIPSOL").getInt(null)) {
						Method grbCallbackGetDoubleInfoMethod = grbCallbackClass.getDeclaredMethod("getDoubleInfo", new Class[]{ int.class });
						Method grbCallbackGetSolutionMethod = grbCallbackClass.getDeclaredMethod("getSolution", new Class[]{ Array.newInstance(grbVarClass, 0).getClass() });
						grbCallbackGetDoubleInfoMethod.setAccessible(true);
						grbCallbackGetSolutionMethod.setAccessible(true);

						Solution solution = new Solution((Double) grbCallbackGetDoubleInfoMethod.invoke(self, new Object[]{ grbClass.getDeclaredField("CB_MIPSOL_OBJ").getInt(null) }), 
								(double[]) grbCallbackGetSolutionMethod.invoke(self, new Object[]{ vars }));
						GDBB.intermediateSolution.add(solution);
					}
					return null;
				}
				
			};
					
			try {
				Method modelSetCallbackMethod = modelClass.getMethod("setCallback", new Class[]{ grbCallbackClass });
				Object callback = factory.create(new Class[0], new Object[0], handler);
				modelSetCallbackMethod.invoke(model, new Object[]{ callback });
				
				Method modelOptimizeMethod = modelClass.getMethod("optimize", null);
				modelOptimizeMethod.invoke(model, null);
				
//				Method modelWriteMethod = modelClass.getMethod("write", new Class[]{ String.class });
//				modelWriteMethod.invoke(model, new Object[]{ "model.lp" });
//				modelWriteMethod.invoke(model, new Object[]{ "model.mps" });
				
				Class<?> grbDoubleAttr = classLoader.loadClass("gurobi.GRB$DoubleAttr");
					
				Enum[] grbDoubleAttrConstants = (Enum[]) grbDoubleAttr.getEnumConstants();
				Enum objValAttr = null;
				for (int i = 0; i < grbDoubleAttrConstants.length; i++)
					if (grbDoubleAttrConstants[i].name() == "ObjVal") {
						objValAttr = grbDoubleAttrConstants[i];
						break;
					}
				
				Method modelGetMethod = modelClass.getMethod("get", new Class[]{ grbDoubleAttr });
				return (Double) modelGetMethod.invoke(model, new Object[]{ objValAttr });
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return Double.NaN;
	}

	public void setEnv(double timeLimit, int numThreads) {
		try {
			Class<?> grbDoubleParam = classLoader.loadClass("gurobi.GRB$DoubleParam");
			Class<?> grbIntParam = classLoader.loadClass("gurobi.GRB$IntParam");	
			Enum[] grbDoubleParamConstants = (Enum[]) grbDoubleParam.getEnumConstants();
			Enum[] grbIntParamConstants = (Enum[]) grbIntParam.getEnumConstants();
			Method envSetDoubleMethod = envClass.getMethod("set", new Class[]{ grbDoubleParam, double.class });
			Method envSetIntMethod = envClass.getMethod("set", new Class[]{ grbIntParam, int.class });
			
			for (int i = 0; i < grbDoubleParamConstants.length; i++) {
				if (grbDoubleParamConstants[i].name().equals("Heuristics")) {
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0 });
				} else if (grbDoubleParamConstants[i].name().equals("ImproveStartGap")) {
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], Double.POSITIVE_INFINITY });
				} else if (grbDoubleParamConstants[i].name().equals("TimeLimit")) {
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], timeLimit});
				}
				/*
				switch (grbDoubleParamConstants[i].name()) {
				case "Heuristics":
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], 1.0 });
					break;
				case "ImproveStartGap":
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], Double.POSITIVE_INFINITY });
					break;
				case "TimeLimit":
					envSetDoubleMethod.invoke(env, new Object[]{ grbDoubleParamConstants[i], timeLimit});
					break;
				}
				*/
			}

			for (int i = 0; i < grbIntParamConstants.length; i++) {
				if (grbIntParamConstants[i].name().equals("MIPFocus")) {
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], 1 });
				} else if (grbIntParamConstants[i].name().equals("Threads")) {
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], numThreads });
				}
				/*
				switch (grbIntParamConstants[i].name()) {
				case "MIPFocus":
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], 1 });
					break;
				case "Threads":
					envSetIntMethod.invoke(env, new Object[]{ grbIntParamConstants[i], numThreads });
					break;
				}
				*/
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public void setObj(Map<Integer, Double> map) {
		try {
			Class<?> grbLinExprClass = classLoader.loadClass("gurobi.GRBLinExpr");
			Class<?> grbVarClass = classLoader.loadClass("gurobi.GRBVar");
			Object expr = grbLinExprClass.newInstance();
			Method exprAddTermMethod = grbLinExprClass.getMethod("addTerm", new Class[]{ double.class, grbVarClass });

			Set<Entry<Integer, Double>> s = map.entrySet();
			Iterator<Entry<Integer, Double>> it = s.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> m = it.next();
				int key = m.getKey();
				Double value = m.getValue();
				Object var = this.vars.get(key);
				exprAddTermMethod.invoke(expr, new Object[]{ value, var });

				//System.out.println("key = " + key + " value = " + value);
				//System.out.println("objType: " + this.objType);
			}

			Class<?> grbExprClass = classLoader.loadClass("gurobi.GRBExpr");
			Method modelSetObjectiveMethod = modelClass.getMethod("setObjective", new Class[]{ grbExprClass, int.class });
			modelSetObjectiveMethod.invoke(model, new Object[]{ expr, getGRBObjType(this.objType) });
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setObjType(ObjType objType) {
		this.objType = objType;
	}
	
	@Override
	public void setVar(String varName, VarType types, double lb, double ub) {		
		if (varName != null && types != null) {
			try {
				Method modelAddVarMethod = modelClass.getMethod("addVar", 
						new Class[]{ double.class, double.class, double.class, char.class, String.class });
				Object var = modelAddVarMethod.invoke(this.model, new Object[]{lb, ub, 0.0, getGRBVarType(types), varName});
				this.vars.add(var);

				Method modelUpdateMethod = modelClass.getMethod("update", null);
				modelUpdateMethod.invoke(this.model, null);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	@Override
	public void setVars(VarType[] types, double[] lb, double[] ub) {
		try {
			Method modelAddVarMethod = modelClass.getMethod("addVar", 
					new Class[]{ double.class, double.class, double.class, char.class, String.class });
			if (types.length == lb.length && lb.length == ub.length) {
				for (int i = 0; i < lb.length; i++) {
					Object var = modelAddVarMethod.invoke(this.model, new Object[]{lb[i], ub[i], 0.0, getGRBVarType(types[i]), Integer.toString(i)});
					this.vars.add(var);
				}

				Method modelUpdateMethod = modelClass.getMethod("update", null);
				modelUpdateMethod.invoke(this.model, null);
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
