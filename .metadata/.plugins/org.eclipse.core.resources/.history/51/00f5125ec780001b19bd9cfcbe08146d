package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		//új scanner a beolvasáshoz
		Scanner sc = new Scanner(System.in);
		//string ami tárolja a beolvasott sort
		String event = "";
		
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		
		s.init();
		s.enter();
		
		while(!event.equals("exit") && sc.hasNext()) {
			event = sc.nextLine();
			
			//megnézi, hogy egyezik-e valamelyik paranccsal, ha igen végrehajtja
			switch(event) {
				case "start":
					s.raiseStart();
					break;
				case "white":
					s.raiseWhite();
					break;
				case "black":
					s.raiseBlack();
					break;
				default:
					break;	
			}
			s.runCycle();
			print(s);
		}
		if(event.equals("exit")) {
			sc.close();
			System.exit(0);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
