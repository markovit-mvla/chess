package control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import model.ChessAI;
import model.ChessModel;
import model.ChessMove;

public class AIThread implements Runnable {
	/*
	 * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html
	 * Did some research on different Queue implementations for the Thread
	 */
	private ChessController control;
	private BlockingQueue<ChessModel> q = new SynchronousQueue<>();
	private ChessAI ai = new ChessAI();
	
	public AIThread(ChessController control) {
		this.control = control;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				ChessModel model = q.poll(1, TimeUnit.DAYS);
				ChessMove aiMove = ai.getOptimalMove(model);
				model.makeMove(aiMove);
				control.moveMade();
			} catch (InterruptedException e) {
				// Should never happen
				e.printStackTrace();
			}			
		}
	}

	public void calculate(ChessModel model) {
		try {
			q.offer(model, 10, TimeUnit.SECONDS);
		} catch (Exception e) {
			// Should never happen
			e.printStackTrace();
		}
	}
}
