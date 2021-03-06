package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.base.types.Event;
import org.yakindu.base.types.Property;
import org.yakindu.sct.model.sgraph.Scope;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		int nev = 0;
		
		//System.out.println("public static void print(IExampleStatemachine s) {");
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				//System.out.println(state.getName());
				//nincs név
				if (state.getName() == "") {
					state.setName("NoName" + nev);
					nev++;
					//System.out.println("Uj név: " + state.getName());
				}
				//csapda állapot-e
				if (state.getOutgoingTransitions().size() == 0) {
					//System.out.println("Csapda állapot: " + state.getName());
					}
				//tranzakciók kiírása
				for (Transition transition : state.getOutgoingTransitions()) {
					State out = (State) transition.getTarget();
					if (out.getName() == "") {
						out.setName("NoName" + nev);
						nev++;
					}
					//System.out.println(state.getName() + " -> " + out.getName());
				}
			}else if(content instanceof Scope) {
				Scope scope = (Scope) content;
				
				for(int i = 0; i < scope.getEvents().size(); i++) {
					Event event = scope.getEvents().get(i);
					if(i == 0)
						System.out.println("public class RunStatechart {\r\n" + 
								"	\r\n" + 
								"	public static void main(String[] args) throws IOException {\r\n" + 
								"		Scanner sc = new Scanner(System.in);\r\n" + 
								"		String event = \"\";\r\n" + 
								"		\r\n" + 
								"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
								"		s.setTimer(new TimerService());\r\n" + 
								"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
								"		\r\n" + 
								"		s.init();\r\n" + 
								"		s.enter();\r\n" + 
								"		\r\n" + 
								"		while(sc.hasNext()) {\r\n" + 
								"			event = sc.nextLine();\r\n" + 
								"			\r\n" + 
								"			if(event.equals(\"exit\")) {\r\n" + 
								"				sc.close();\r\n" + 
								"				System.exit(0);\r\n" + 
								"			}\r\n" + 
								"			switch(event) {");
					System.out.println(
							"				case \"" + event.getName() + "\":\r\n" + 
							"					s.raise" + event.getName() + "();\r\n" + 
							"					break;");
					
					if(i == scope.getEvents().size()-1) {
						System.out.println("			}\r\n" + 
								"			s.runCycle();");
						for(int j = 0; j < scope.getVariables().size(); j++){
							Property prop = scope.getVariables().get(j);
							//a változó nevét írom ki, hogy megváltoztatott változónevekkel is értelmes legyen a kód
							System.out.println("			System.out.println(\"" + prop.getName() + "= \" + s.getSCInterface().get" + prop.getName() + "());");
						} 
						System.out.println(
								"		}\r\n" + 
								"	}\r\n}");
					}
				}
			}
		}
		
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
