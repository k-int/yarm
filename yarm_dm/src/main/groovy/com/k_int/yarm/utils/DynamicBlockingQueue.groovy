package com.k_int.yarm.utils

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor

/**
 *  taken from http://groovy-programming.com/post/26923146865
 */
@groovy.transform.TupleConstructor
class DynamicBlockingQueue<E> {
    
    @groovy.lang.Delegate
    BlockingQueue<E> queue
    
    ThreadPoolExecutor executor

    @groovy.transform.Synchronized
    boolean offer(def e) {
        !executor || executor.poolSize == executor.maximumPoolSize ?  queue.offer(e) : false
    }

}
