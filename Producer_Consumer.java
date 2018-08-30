/*
 * ProducerConsumer.java
 *
 * This file illustrates the use of Threads and monitor lock in Java.
 *
 *  Created on: Aug 5, 2018
 *      Author: Xinmeng Zhang
 */

/**
 * ProducerConsumer class has two nested static classes, Producer and Consumer
 * While Producer fill in a stringbuilder with current time, Consumer clean
 * up the stringbuilder 
 */
public class ProducerConsumer {
	
	/** a static StringBuilder instance sb*/
	static StringBuilder sb = new StringBuilder();
	
	/**
	 * Class Producer is a Runnable whose run() method loops 20 times. For each iteration, 
	 * the method:
	 * acquires a lock on sb
	 * while sb is not empty, calls sb.wait() to wait for the Consumer to empty sb
	 * sets sb to a string that represents the current time in milliseconds
	 * calls sb.notify() to notify that a new string is available
	 * releases the lock on sb
	 * sleeps for 2 seconds to simulate a slow producer
	 */
	public static class Producer implements Runnable{
		
		/**
		 * Override of run function
		 */
		@Override
		public void run() {
			int i = 0;
			// loop for 20 times
			while (i<20) {
				synchronized(sb) {
					// lock on sb
					System.err.printf("Producer: locked sb\n");
					// while sb is not empty, wait
					while (sb.length() != 0) {
						try {
							sb.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// set sb to a string represents current time
					long time = System.currentTimeMillis();
					sb.append(Long.toString(time));
					// notify a new string is avaliable
					sb.notify();
				}
				// release lock
				System.err.printf("Producer: unlocking sb\n");
				try {
					// sleep for two seconds 
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	
	}
	
	/**
	 * Class Consumer is also a Runnable. Its run() method also loops 20 times. 
	 * For each iteration, the method:
	 * acquires a lock on sb
	 * while sb is empty, calls sb.wait() to wait for the Producer to set sb.
	 * prints the sb string to the console
	 * empties sb by setting its length to 0
	 * calls sb.notify() to notify that the buffer is empty
	 * releases the lock on sb
	 */
	public static class Consumer implements Runnable{

		/**
		 * Override of run function
		 */
		@Override
		public void run() {
			int j = 0;
			// loop 20 times
			while (j<20) {
				synchronized(sb) {
					// lock on sb
					System.err.printf("Consumer: locked sb\n");
					// while sb is empty, wait
					while (sb.length() == 0) {
						try {
							sb.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// print sb
					System.err.printf("sb: %s\n", sb); // if use sout, will overlap with err
					// clean up sb
					sb.setLength(0);
					// notify the buffer is empty
					sb.notify();
				}
				// release lock
				System.err.printf("Consumer: unlocking sb\n");
				j++;
			}
			
		}	
	}
	
	/**
	 * a main() method that creates and starts two threads with the Runnables. 
	 * It then waits for both threads to terminate using Thread.join()
	 * @param args the arguements
	 */
	public static void main(String args[]) {
		// create two threads
		Thread producer = new Thread(new Producer(), "producer"); 
		Thread consumer = new Thread(new Consumer(), "consumer");;  
   
        producer.start(); //start producer first to fill in sb
        consumer.start();
        // wait for both threads to terminate using Thread.join();
        try {
        	producer.join();
        } catch(InterruptedException e) {
            System.out.println(e);
        }
        try {
        	consumer.join();
        } catch(InterruptedException e) {
            System.out.println(e);
        }
        System.err.println("Finished\n");
     }
}
