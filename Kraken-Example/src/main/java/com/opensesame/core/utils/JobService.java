package com.opensesame.core.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobService {
	
	private static ExecutorService executor = Executors.newCachedThreadPool();

	public static void push(Callable<Object> job) {/*Promise*/
		CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
			try {
				return job.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}, executor);

//		return new Promise(future, executor);
	}
	public static void main(String[] args) {
		
	}

}
